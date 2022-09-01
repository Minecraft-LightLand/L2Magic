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

public record NodalItemHandler(ItemNodeEntity be) implements IItemHandler, ItemStackNode {

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
		return be.getConnector();
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
		for (BlockPos pos : be.getConnector().getAvailableTarget()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				var lazyCap = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					if (cap instanceof ItemStackNode node) {
						ans.add(new SimpleNodeSupplier<>(pos, node.isReady(), (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token)));
					} else {
						ans.add(new SimpleNodeSupplier<>(pos, true, (ctx, token) -> new ItemNodeTarget(target, cap, token)));
					}
					continue;
				}
			}
			ans.add(new SimpleNodeSupplier<>(pos, false, (ctx, token) -> new ErrorNode<>(pos)));
		}
		return ans;
	}

	@Override
	public void refreshCooldown(BlockPos target, boolean success, TransportContext<ItemStack> ctx) {
		be.refreshCoolDown(target, success, ctx.simulate);
	}

	@Override
	public BlockPos getIdentifier() {
		return be.getBlockPos();
	}

	@Override
	public boolean isReady() {
		return be.getConnector().isReady();
	}

}
