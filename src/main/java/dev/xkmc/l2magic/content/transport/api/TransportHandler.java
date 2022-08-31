package dev.xkmc.l2magic.content.transport.api;

import dev.xkmc.l2magic.content.transport.core.GenericToken;

public class TransportHandler {

	public static <T> INetworkNode<T> broadcastRecursive(TransportContext<T> ctx, INodeHolder<T> node, IContentToken<T> token) {
		if (!node.isValid(token.get())) {
			return BroadcastTree.empty(node, token);
		}
		BroadcastTree.Builder<T> root = BroadcastTree.constructRoot(node, token);
		root.iterate(ctx, node.getTargets());
		return root.build();
	}

	public static <T> T insert(INodeHolder<T> node, IContentHolder<T> holder, boolean simulate) {
		TransportContext<T> ctx = new TransportContext<>();
		IContentToken<T> token = new GenericToken<>(holder);
		INetworkNode<T> tree = TransportHandler.broadcastRecursive(ctx, node, token);
		if (!simulate) {
			tree.refreshCoolDown(tree.hasAction());
		}
		if (!tree.hasAction()) {
			return holder.get();
		}
		if (!simulate) {
			RealToken<T> real = holder.toReal();
			tree.perform(real);
			return real.getRemain();
		}
		return token.getRemain();
	}

}
