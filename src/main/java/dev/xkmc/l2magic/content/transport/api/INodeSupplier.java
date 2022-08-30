package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

public interface INodeSupplier<T> {

	/**
	 * construct the network node: test its sub nodes against the content
	 */
	INetworkNode<T> constructNode(TransportContext<T> ctx, IContentToken<T> token);

	/**
	 * return the block position of the node it represents
	 */
	BlockPos getIdentifier();

}
