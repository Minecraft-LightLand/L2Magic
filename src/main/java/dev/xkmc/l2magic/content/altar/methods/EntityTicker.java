package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.OnReplacedBlockMethod;
import dev.xkmc.l2library.block.mult.ScheduleTickBlockMethod;
import dev.xkmc.l2library.block.mult.ShapeUpdateBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EntityTicker implements ScheduleTickBlockMethod, OnReplacedBlockMethod {

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.getBlockEntity(pos) instanceof DelayedTickerBlockEntity ticker) {
			ticker.update();
		}
	}

	@Override
	public void onReplaced(BlockState state, Level level, BlockPos pos, BlockState other, boolean isPiston) {
		if (level.getBlockEntity(pos) instanceof DelayedTickerBlockEntity ticker) {
			ticker.blockRemoved();
		}
	}
}
