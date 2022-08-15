package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.PotionSpellRecipe;
import dev.xkmc.l2magic.init.registrate.LLRecipes;

public class PotionSpellBuilder extends AbstractRitualBuilder<PotionSpellBuilder, PotionSpellRecipe> {

	public PotionSpellBuilder() {
		super(LLRecipes.RSP_SPELL.get());
	}

}
