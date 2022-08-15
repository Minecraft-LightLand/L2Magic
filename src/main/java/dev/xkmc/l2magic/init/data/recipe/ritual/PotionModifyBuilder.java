package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.PotionModifyRecipe;
import dev.xkmc.l2magic.init.registrate.LLRecipes;

public class PotionModifyBuilder extends AbstractRitualBuilder<PotionModifyBuilder, PotionModifyRecipe> {

	public PotionModifyBuilder() {
		super(LLRecipes.RSP_MODIFY.get());
	}

}
