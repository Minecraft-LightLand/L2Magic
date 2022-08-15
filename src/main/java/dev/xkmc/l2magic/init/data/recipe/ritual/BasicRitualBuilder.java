package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2magic.content.magic.ritual.BasicRitualRecipe;
import dev.xkmc.l2magic.init.registrate.LMRecipes;

public class BasicRitualBuilder extends AbstractRitualBuilder<BasicRitualBuilder, BasicRitualRecipe> {

	public BasicRitualBuilder() {
		super(LMRecipes.RS_DEF.get());
	}

}
