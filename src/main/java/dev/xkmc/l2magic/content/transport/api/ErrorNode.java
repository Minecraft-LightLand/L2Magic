package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

public record ErrorNode<T>(BlockPos pos) implements INetworkNode<T> {

	@Override
	public int getConsumed() {
		return 0;
	}

	@Override
	public void refreshCoolDown(boolean success) {

	}

	@Override
	public void perform(RealToken<T> token) {

	}

	@Override
	public boolean hasAction() {
		return false;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}

}
