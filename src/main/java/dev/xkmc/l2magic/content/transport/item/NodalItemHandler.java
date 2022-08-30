package dev.xkmc.l2magic.content.transport.item;

import dev.xkmc.l2magic.content.transport.api.*;
import dev.xkmc.l2magic.content.transport.core.ItemStackNode;
import dev.xkmc.l2magic.content.transport.core.SimpleNodeSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record NodalItemHandler(NodeBlockEntity<?> be) implements IItemHandler, ItemStackNode {

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		return TransportHandler.insert(this, new ItemHolder(stack), simulate);
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return be.isItemStackValid(stack);
	}

	@Override
	public NetworkType getNetworkType() {
		return be.getNetworkType();
	}

	@Override
	public boolean isValid(IContentHolder<ItemStack> token) {
		return be.isItemStackValid(token.get());
	}

	@Override
	public List<INodeSupplier<ItemStack>> getTargets() {
		Level level = be.getLevel();
		if (level == null) return List.of();
		List<INodeSupplier<ItemStack>> ans = new ArrayList<>();
		for (BlockPos pos : be.target()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target == null) continue;
			var lazyCap = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			if (lazyCap.resolve().isPresent()) {
				var cap = lazyCap.resolve().get();
				if (cap instanceof ItemStackNode node) {
					ans.add(new SimpleNodeSupplier<>(pos, (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token)));
				} else {
					ans.add(new SimpleNodeSupplier<>(pos, (ctx, token) -> new ItemNodeTarget(target, cap, token)));
				}
			}
		}
		return ans;
	}

	@Override
	public void refreshCooldown(TransportContext<ItemStack> ctx) {
		be.refreshCooldown(ctx);
	}

	@Override
	public void setCoolDownType(CoolDownType type) {
		be.setCoolDownType(type);
	}

	@Override
	public BlockPos getIdentifier() {
		return be.getBlockPos();
	}

}
