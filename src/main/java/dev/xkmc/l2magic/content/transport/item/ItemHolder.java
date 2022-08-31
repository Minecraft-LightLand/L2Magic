package dev.xkmc.l2magic.content.transport.item;

import dev.xkmc.l2magic.content.transport.api.IContentHolder;
import net.minecraft.world.item.ItemStack;

public record ItemHolder(ItemStack stack) implements IContentHolder<ItemStack> {

	@Override
	public int getCount() {
		return stack.getCount();
	}

	@Override
	public ItemStack get() {
		return stack;
	}

	@Override
	public ItemStack getCopy(int count) {
		if (count <= 0) return ItemStack.EMPTY;
		ItemStack copy = stack.copy();
		copy.setCount(count);
		return copy;
	}

	@Override
	public ItemStack empty() {
		return ItemStack.EMPTY;
	}

}
