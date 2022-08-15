package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.PotionModifyRecipe;
import dev.xkmc.l2magic.init.registrate.LMRecipes;

public class PotionModifyBuilder extends AbstractRitualBuilder<PotionModifyBuilder, PotionModifyRecipe> {

	public PotionModifyBuilder() {
		super(LMRecipes.RSP_MODIFY.get());
	}

}
