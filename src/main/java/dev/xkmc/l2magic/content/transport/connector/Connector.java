package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2magic.content.transport.api.NetworkType;
import dev.xkmc.l2magic.content.transport.tile.base.CoolDownType;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public interface Connector extends NetworkType {

	List<BlockPos> getConnected();

	List<BlockPos> getAvailableTarget();

	void perform();

	void link(BlockPos pos);

	void removeIf(Predicate<BlockPos> o);

	int getMaxCoolDown(BlockPos pos);

	int getCoolDown(BlockPos pos);

	void tick();

	boolean isReady();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	CoolDownType getType(BlockPos pos);

	@Override
	default boolean testConsumption(int avail, int c) {
		return true;
	}

	@Override
	default boolean shouldContinue(int available, int consumed, int size) {
		return provide(available, consumed, size) > 0;
	}

	@Override
	default int provide(int available, int consumed, int size) {
		return Math.max(0, available - consumed);
	}

}
