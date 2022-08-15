package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.PotionBoostRecipe;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class PotionBoostBuilder extends AbstractLevelRitualBuilder<PotionBoostBuilder, PotionBoostRecipe> {

	public PotionBoostBuilder(MobEffect effect, int modify, ResourceLocation magic, int... levels) {
		super(LMRecipes.RSP_BOOST.get(), magic, levels);
		recipe.effect = effect;
		recipe.modify_level = modify;
	}
}
