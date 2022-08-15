package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.base.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

@SerialClass
public class AbstractLevelRitualRecipe<R extends AbstractLevelRitualRecipe<R>> extends AbstractRitualRecipe<R> {

	@SerialClass.SerialField
	public ResourceLocation magic_recipe;

	@SerialClass.SerialField
	public int[] levels;

	public AbstractLevelRitualRecipe(ResourceLocation id, BaseRecipe.RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> fac) {
		super(id, fac);
	}

	@Nullable
	public ResourceLocation getMagic() {
		return magic_recipe;
	}

	public int getLevel(int cost) {
		for (int i = 0; i < levels.length; i++) {
			if (cost > levels[i]) {
				return i;
			}
		}
		return levels.length;
	}

	public int getNextLevel(int cost) {
		for (int level : levels) {
			if (cost > level) {
				return level;
			}
		}
		return 0;
	}

}
