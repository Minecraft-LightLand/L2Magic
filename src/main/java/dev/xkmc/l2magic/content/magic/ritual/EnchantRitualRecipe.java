package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

@SerialClass
public class EnchantRitualRecipe extends AbstractLevelRitualRecipe<EnchantRitualRecipe> {

	public EnchantRitualRecipe(ResourceLocation id) {
		super(id, LMRecipes.RS_ENCH.get());
	}

	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack stack = assemble(inv);
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		map.replaceAll((e, v) -> v + level - 1);
		EnchantmentHelper.setEnchantments(map, stack);
		inv.setItem(5, stack);
	}

}
