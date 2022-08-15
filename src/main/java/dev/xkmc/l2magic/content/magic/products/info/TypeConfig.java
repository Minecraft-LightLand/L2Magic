package dev.xkmc.l2magic.content.magic.products.info;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record TypeConfig(Item icon, ResourceLocation background) {

	public ItemStack getIcon() {
		return icon.getDefaultInstance();
	}

}
