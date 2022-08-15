package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.block.RitualSide;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SerialClass
public class PotionCoreRecipe extends AbstractRitualRecipe<PotionCoreRecipe> {

	public PotionCoreRecipe(ResourceLocation id) {
		super(id, LMRecipes.RSP_CORE.get());
	}

	@Override
	public void assemble(RitualCore.Inv inv, int level) {
		Map<MobEffect, MobEffectInstance> map = new HashMap<>();
		for (RitualSide.TE te : inv.sides) {
			ItemStack stack = te.getItem(0);
			if (stack.getItem() == Items.POTION) {
				for (MobEffectInstance ins : PotionUtils.getMobEffects(te.getItem(0))) {
					map.put(ins.getEffect(), ins);
				}
			}
		}
		ItemStack stack = assemble(inv);
		PotionUtils.setCustomEffects(stack, new ArrayList<>(map.values()));
		inv.setItem(5, stack);
	}

}
