package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.PlacementBlockMethod;
import dev.xkmc.l2library.block.mult.ScheduleTickBlockMethod;
import dev.xkmc.l2magic.content.altar.tile.structure.StructureDebugHandler;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class AltarPillarState implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod, ScheduleTickBlockMethod {

	public static final BooleanProperty GROUNDED = BooleanProperty.create("grounded");
	public static final EnumProperty<PillarStatus> PILLAR = EnumProperty.create("pillar", PillarStatus.class, PillarStatus.values());

	private final PillarStatus type;

	public AltarPillarState(PillarStatus type) {
		this.type = type;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(GROUNDED);
		builder.add(PILLAR);
	}

	private BlockState updateShape(BlockState selfState, LevelAccessor level, BlockPos selfPos) {
		if (type == PillarStatus.DARK) {
			BlockState topState = level.getBlockState(selfPos.above());
			boolean topConnect = topState.is(LMBlocks.ALTAR_TABLE.get());
			boolean pillarConnect = topState.is(LMBlocks.ALTAR_PILLAR.get()) && topState.getValue(PILLAR).connectsTable();
			boolean topPower = topState.is(LMBlocks.ALTAR_HOLDER.get());
			boolean pillarPower = topState.is(LMBlocks.ALTAR_PILLAR.get()) && topState.getValue(PILLAR).connectsHolder();
			selfState = selfState.setValue(PILLAR, topConnect || pillarConnect ?
					PillarStatus.CONNECTED : topPower || pillarPower ?
					PillarStatus.POWERED : PillarStatus.DARK);
		}
		BlockState belowState = level.getBlockState(selfPos.below());
		boolean baseGround = belowState.is(LMBlocks.ALTAR_BASE.get()) && (selfState.getValue(PILLAR).connectsHolder() || AltarBaseState.isPowered(belowState));
		boolean pillarGround = belowState.is(LMBlocks.ALTAR_PILLAR.get()) && belowState.getValue(GROUNDED);
		selfState = selfState.setValue(GROUNDED, baseGround || pillarGround);
		return selfState;
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		LevelAccessor level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return this.updateShape(state, level, pos);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(GROUNDED, false).setValue(PILLAR, type.getDefault());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockState newState = updateShape(state, level, pos);
		if (newState != state) {
			level.setBlockAndUpdate(pos, newState);
			StructureDebugHandler.info("set pillar to " + newState + " at " + pos);
		}
	}

}
