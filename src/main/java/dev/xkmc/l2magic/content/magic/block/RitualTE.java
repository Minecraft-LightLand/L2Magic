package dev.xkmc.l2magic.content.magic.block;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class RitualTE extends SyncedSingleItemTE {

	public RitualTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dire) {
		return super.canPlaceItemThroughFace(slot, stack, dire);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dire) {
		return super.canTakeItemThroughFace(slot, stack, dire);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		sync();
	}

	@Override
	public boolean isLocked() {
		return getBlockState().getValue(BlockStateProperties.LIT);
	}

	protected void setLocked(boolean bool) {
		if (level != null) {
			level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.LIT, bool), 3);
		}
	}

	public static class RitualPlace implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod {

		@Override
		public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			BlockEntity te = level.getBlockEntity(pos);
			if (te instanceof RitualTE rte) {
				if (!rte.isLocked()) {
					if (rte.isEmpty()) {
						if (!pl.getMainHandItem().isEmpty()) {
							rte.setItem(0, pl.getMainHandItem().split(1));
						}
					} else {
						pl.getInventory().placeItemBackInInventory(rte.removeItem(0, 1));
					}
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

}
