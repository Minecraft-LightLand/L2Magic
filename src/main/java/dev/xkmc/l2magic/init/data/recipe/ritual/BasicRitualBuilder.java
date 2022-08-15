package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.BasicRitualRecipe;
import dev.xkmc.l2magic.init.registrate.LLRecipes;

public class BasicRitualBuilder extends AbstractRitualBuilder<BasicRitualBuilder, BasicRitualRecipe> {

	public BasicRitualBuilder() {
		super(LLRecipes.RS_DEF.get());
	}

}
