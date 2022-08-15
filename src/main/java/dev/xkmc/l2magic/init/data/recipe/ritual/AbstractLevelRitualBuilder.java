package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2library.base.recipe.BaseRecipe;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.ritual.AbstractLevelRitualRecipe;
import dev.xkmc.l2magic.content.magic.ritual.AbstractRitualRecipe;
import net.minecraft.resources.ResourceLocation;

public class AbstractLevelRitualBuilder<B extends AbstractRitualBuilder<B, R>, R extends AbstractLevelRitualRecipe<R>>
		extends AbstractRitualBuilder<B, R> {

	public AbstractLevelRitualBuilder(BaseRecipe.RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> type,
									  ResourceLocation magic, int... levels) {
		super(type);
		recipe.magic_recipe = magic;
		recipe.levels = levels;
	}

}
