package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

record BroadcastTree<T>(INodeHolder<T> node, List<INetworkNode<T>> children, IContentHolder<T> token,
						int consumed) implements INetworkNode<T> {

	public static <T> INetworkNode<T> empty(INodeHolder<T> node, IContentToken<T> token) {
		return new BroadcastTree<>(node, List.of(), token.get(), 0);
	}

	public static <T> Builder<T> constructRoot(INodeHolder<T> node, IContentToken<T> token) {
		return new Builder<>(node, token);
	}

	public int getConsumed() {
		return consumed;
	}

	public void refreshCoolDown(TransportContext<T> ctx) {
		node.refreshCooldown(ctx);
		for (INetworkNode<T> child : children) {
			child.refreshCoolDown(ctx);
			CoolDownType type = child.hasAction() ? CoolDownType.SUCCESS : CoolDownType.FAIL;
			if (ctx.hasError() && ctx.evaluate(node.getIdentifier(), child.getIdentifier()))
				type = CoolDownType.COLLISION;
			node.setCoolDownType(type);
		}
	}

	public void perform(RealToken<T> token) {
		if (!hasAction()) return;
		for (INetworkNode<T> child : children) {
			child.perform(token);
		}
	}

	public boolean hasAction() {
		return consumed > 0;
	}

	@Override
	public BlockPos getIdentifier() {
		return node.getIdentifier();
	}

	public static class Builder<T> implements IContentToken<T> {

		public final NetworkType type;

		private final INodeHolder<T> node;
		private final IContentToken<T> token;
		private final List<INetworkNode<T>> children = new ArrayList<>();

		private int consumed = 0;
		private boolean valid = true;

		private Builder(INodeHolder<T> node, IContentToken<T> token) {
			this.type = node.getNetworkType();
			this.node = node;
			this.token = token;
		}

		public void append(TransportContext<T> ctx, INodeSupplier<T> factory) {
			ctx.push(factory.getIdentifier());
			if (!ctx.hasError()) {
				INetworkNode<T> node = factory.constructNode(ctx, this);
				int c = node.getConsumed();
				if (type == NetworkType.ALL && c != 1) {
					valid = false;
				}
				children.add(node);
			}
			ctx.pop();
		}

		public INetworkNode<T> build() {
			int val = 0;
			if (valid && token.getAvailable() >= consumed) {
				token.consume(consumed);
				val = consumed;
			}
			return new BroadcastTree<>(node, children, token.get(), val);
		}

		public boolean shouldContinue() {
			return type == NetworkType.ALL || token.getAvailable() > 0;
		}

		@Override
		public IContentHolder<T> get() {
			return token.get();
		}

		@Override
		public int getAvailable() {
			if (type == NetworkType.ONE)
				return token.getAvailable() - consumed;
			else return 1;
		}

		@Override
		public void consume(int count) {
			consumed += count;
		}

	}

}
