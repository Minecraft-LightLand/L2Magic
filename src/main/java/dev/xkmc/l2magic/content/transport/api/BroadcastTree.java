package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

import java.util.List;

record BroadcastTree<T>(INodeHolder<T> node, List<INetworkNode<T>> children, IContentHolder<T> token,
						int consumed) implements INetworkNode<T> {

	public static <T> INetworkNode<T> empty(INodeHolder<T> node, IContentToken<T> token) {
		return new BroadcastTree<>(node, List.of(), token.get(), 0);
	}

	public static <T> TreeBuilder<T> constructRoot(INodeHolder<T> node, IContentToken<T> token) {
		return new TreeBuilder<>(node, token);
	}

	public int getConsumed() {
		return consumed;
	}

	public void refreshCoolDown(TransportContext<T> ctx, boolean success) {
		for (INetworkNode<T> child : children) {
			boolean subSuc = success && child.hasAction();
			node.refreshCooldown(child.getIdentifier(), subSuc, ctx);
			child.refreshCoolDown(ctx, subSuc);
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

}
