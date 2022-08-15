package dev.xkmc.l2magic.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.init.registrate.LMRecipes;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class BasicRitualRecipe extends AbstractRitualRecipe<BasicRitualRecipe> {

	public BasicRitualRecipe(ResourceLocation id) {
		super(id, LMRecipes.RS_DEF.get());
	}
}
