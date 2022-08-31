package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

public interface INetworkNode<T> {

	/**
	 * get count of content consumed by this node and its sub-node
	 */
	int getConsumed();

	/**
	 * refresh the cooldown of the node
	 */
	void refreshCoolDown(TransportContext<T> ctx, boolean success);

	/**
	 * perform transaction guarded by a real token
	 */
	void perform(RealToken<T> token);

	/**
	 * return true if transaction could happen
	 */
	boolean hasAction();

	/**
	 * return the block position of the node it represents
	 */
	BlockPos getIdentifier();
}
