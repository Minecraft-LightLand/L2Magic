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

	public void refreshCoolDown(boolean success) {
		for (INetworkNode<T> child : children) {
			boolean subSuc = success && child.hasAction();
			node.refreshCooldown(child.getIdentifier(), subSuc);
			child.refreshCoolDown(subSuc);
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

		public void iterate(TransportContext<T> ctx, List<INodeSupplier<T>> targets) {
			for (INodeSupplier<T> pos : targets) {
				append(ctx, pos);
				if (!shouldContinue()) break;
			}
		}

		public void append(TransportContext<T> ctx, INodeSupplier<T> factory) {
			INetworkNode<T> node;
			if (factory.isValid() && ctx.add(factory.getIdentifier())) {
				node = factory.constructNode(ctx, this);
			} else {
				node = new ErrorNode<>(factory.getIdentifier());
			}
			int c = node.getConsumed();
			if (type == NetworkType.ALL) {
				if (c != 1) {
					valid = false;
				}
			}
			children.add(node);
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
			if (type == NetworkType.ALL)
				return 1;
			return token.getAvailable() - consumed;
		}

		@Override
		public void consume(int count) {
			consumed += count;
		}

	}

}
