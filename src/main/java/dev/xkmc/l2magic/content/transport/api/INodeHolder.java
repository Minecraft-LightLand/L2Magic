package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface INodeHolder<T> {

	/**
	 * get the network type of this node: send to some nodes or all nodes?
	 */
	NetworkType getNetworkType();

	/**
	 * check if the content is valid for this node. For example, filters
	 */
	boolean isValid(IContentHolder<T> token);

	/**
	 * get all targeting blocks
	 */
	List<INodeSupplier<T>> getTargets();

	/**
	 * refresh cool down for this node
	 */
	void refreshCooldown(TransportContext<T> ctx);

	/**
	 * set the state of this node
	 */
	void setCoolDownType(CoolDownType type);

	/**
	 * return the block position of the node it represents
	 */
	BlockPos getIdentifier();
}
