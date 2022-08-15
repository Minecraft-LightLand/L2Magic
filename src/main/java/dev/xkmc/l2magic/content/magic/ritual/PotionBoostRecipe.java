package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.init.registrate.LLRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class PotionBoostRecipe extends AbstractLevelRitualRecipe<PotionBoostRecipe> {

	@SerialClass.SerialField
	public MobEffect effect;

	@SerialClass.SerialField
	public int modify_level;

	public PotionBoostRecipe(ResourceLocation id) {
		super(id, LLRecipes.RSP_BOOST.get());
	}

	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack stack = inv.core.getItem(0).copy();
		List<MobEffectInstance> list = new ArrayList<>();
		for (MobEffectInstance ins : PotionUtils.getCustomEffects(stack)) {
			if (effect == ins.getEffect()) {
				if (ins.getAmplifier() < level) {
					if (modify_level == -1)
						continue;
					list.add(new MobEffectInstance(ins.getEffect(), ins.getDuration(), level - 1));
					continue;
				}
			}
			list.add(ins);
		}
		PotionUtils.setCustomEffects(stack, list);
		assemble(inv);
		inv.setItem(5, stack);
	}

}
