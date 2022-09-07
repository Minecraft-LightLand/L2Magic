package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.PlacementBlockMethod;
import dev.xkmc.l2library.block.mult.ShapeUpdateBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DelayedTicker implements ShapeUpdateBlockMethod, PlacementBlockMethod {

	@Override
	public BlockState updateShape(Block self, BlockState currentState, BlockState oldState, Direction from, BlockState sourceState, LevelAccessor level, BlockPos selfPos, BlockPos sourcePos) {
		update(self, level, selfPos);
		return currentState;
	}

	@Override
	public BlockState getStateForPlacement(BlockState blockState, BlockPlaceContext ctx) {
		update(blockState.getBlock(), ctx.getLevel(), ctx.getClickedPos());
		return blockState;
	}

	public static void update(Block self, LevelAccessor level, BlockPos pos) {
		if (!level.getBlockTicks().hasScheduledTick(pos, self))
			level.scheduleTick(pos, self, 2);
	}

}
