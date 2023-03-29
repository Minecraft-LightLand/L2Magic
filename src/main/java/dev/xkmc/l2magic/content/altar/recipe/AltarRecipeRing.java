package dev.xkmc.l2magic.content.altar.recipe;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@SerialClass
public class AltarRecipeRing {

	@SerialClass.SerialField
	public Ingredient ingredient;

	@SerialClass.SerialField
	public int count;

	@SerialClass.SerialField
	public ItemStack remain;

}
