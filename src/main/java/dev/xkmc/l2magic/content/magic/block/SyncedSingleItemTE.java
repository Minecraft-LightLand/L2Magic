package dev.xkmc.l2magic.content.magic.block;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class SyncedSingleItemTE extends BaseBlockEntity implements WorldlyContainer {

	private static final int[] SLOTS = {0};

	@SerialClass.SerialField(toClient = true)
	public ItemStack stack = ItemStack.EMPTY;

	public SyncedSingleItemTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean isLocked() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(Direction dire) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dire) {
		return !isLocked() && slot == 0 && this.stack.isEmpty();
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dire) {
		return !isLocked() && slot == 0;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return slot == 0 ? stack : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack ans = stack;
		stack = ItemStack.EMPTY;
		setChanged();
		return ans;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack ans = stack;
		stack = ItemStack.EMPTY;
		return ans;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == 0) {
			if (stack.isEmpty()) {
				this.stack = ItemStack.EMPTY;
			} else {
				this.stack = stack.split(1);
			}
			setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		stack = ItemStack.EMPTY;
		setChanged();
	}

	@SuppressWarnings({"unchecked", "unsafe"})
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (!this.remove && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (LazyOptional<T>) LazyOptional.of(() -> new SidedInvWrapper(this, side));
		return super.getCapability(cap, side);
	}
}

