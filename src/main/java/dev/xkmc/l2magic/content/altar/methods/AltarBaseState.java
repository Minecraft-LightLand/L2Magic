package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.PlacementBlockMethod;
import dev.xkmc.l2library.block.mult.ScheduleTickBlockMethod;
import dev.xkmc.l2magic.content.altar.tile.structure.StructureDebugHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class AltarBaseState implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod, ScheduleTickBlockMethod {

	private static final int MAX = 31;

	public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, MAX);

	public static boolean isPowered(BlockState state) {
		return state.getValue(DISTANCE) < MAX;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DISTANCE);
	}

	private BlockState updateShape(BlockState selfState, LevelAccessor level, BlockPos selfPos) {
		int oldDist = selfState.getValue(DISTANCE);
		if (oldDist == 0) return selfState;
		int dist = MAX;
		for (int i = 0; i < 4; i++) {
			Direction dire = Direction.from2DDataValue(i);
			BlockState neighbor = level.getBlockState(selfPos.relative(dire));
			if (neighbor.is(selfState.getBlock()))
				dist = Math.min(dist, neighbor.getValue(DISTANCE));
		}
		if (oldDist < dist) dist = MAX;
		return selfState.setValue(DISTANCE, Math.min(MAX, dist + 1));
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		LevelAccessor level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return this.updateShape(state, level, pos);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(DISTANCE, MAX);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockState newState = updateShape(state, level, pos);
		if (newState != state) {
			level.setBlockAndUpdate(pos, newState);
			StructureDebugHandler.info("set base to " + newState + " at " + pos);
		}
	}

}
