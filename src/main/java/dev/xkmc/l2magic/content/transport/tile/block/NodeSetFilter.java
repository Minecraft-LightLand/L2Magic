package dev.xkmc.l2magic.content.transport.tile.block;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.l2magic.content.transport.tile.item.AbstractItemNodeBlockEntity;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class NodeSetFilter implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod {

	public static final NodeSetFilter INSTANCE = new NodeSetFilter();

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (pl.getMainHandItem().is(LMItems.LINKER.get()))
			return InteractionResult.PASS;
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity te = level.getBlockEntity(pos);
		if (te instanceof AbstractItemNodeBlockEntity<?> rte) {
			if (rte.filter.isEmpty()) {
				if (!pl.getMainHandItem().isEmpty()) {
					rte.filter = pl.getMainHandItem().split(1);
					level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
				}
			} else {
				pl.getInventory().placeItemBackInInventory(rte.filter);
				rte.filter = ItemStack.EMPTY;
				level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.LIT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.LIT, false);
	}

}