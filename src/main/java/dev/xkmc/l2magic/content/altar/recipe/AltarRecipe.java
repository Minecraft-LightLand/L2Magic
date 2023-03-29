package dev.xkmc.l2magic.content.altar.recipe;

import dev.xkmc.l2library.base.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

@SerialClass
public class AltarRecipe<R extends AltarRecipe<R>> extends BaseRecipe<R, AltarRecipe<?>, AltarContainer> {

	@SerialClass.SerialField
	public ArrayList<AltarRecipeRing> ingredients = new ArrayList<>();

	@SerialClass.SerialField
	public ItemStack result = ItemStack.EMPTY;

	public AltarRecipe(ResourceLocation id, RecType<R, AltarRecipe<?>, AltarContainer> fac) {
		super(id, fac);
	}

	@Override
	public boolean matches(AltarContainer altar, Level level) {
		return altar.manager.ringMatch(ingredients, true);
	}

	@Override
	public ItemStack assemble(AltarContainer altar) {
		altar.manager.ringMatch(ingredients, false);
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

}
