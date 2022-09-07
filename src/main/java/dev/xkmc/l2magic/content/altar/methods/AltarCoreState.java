package dev.xkmc.l2magic.content.altar.methods;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.PlacementBlockMethod;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class AltarCoreState implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod {

	public static final EnumProperty<CoreStatus> STATUS = EnumProperty.create("status", CoreStatus.class);

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STATUS);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(STATUS, CoreStatus.IDLE);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		return state;
	}

}
