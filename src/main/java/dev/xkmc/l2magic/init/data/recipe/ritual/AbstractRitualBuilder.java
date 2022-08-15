package dev.xkmc.l2magic.init.data.recipe.ritual;

import dev.xkmc.l2library.base.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.base.recipe.BaseRecipe;
import dev.xkmc.l2library.base.recipe.BaseRecipeBuilder;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import dev.xkmc.l2magic.content.magic.ritual.AbstractRitualRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;

public class AbstractRitualBuilder<B extends AbstractRitualBuilder<B, R>, R extends AbstractRitualRecipe<R>>
		extends BaseRecipeBuilder<B, R, AbstractRitualRecipe<?>, RitualCore.Inv> {

	public AbstractRitualBuilder(BaseRecipe.RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> type) {
		super(type);
		recipe.core = new AbstractRitualRecipe.Entry();
		recipe.side = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			recipe.side.add(new AbstractRitualRecipe.Entry());
		}
	}

	public B setCore(Ingredient in, ItemStack out) {
		recipe.core.input = in;
		recipe.core.output = out;
		return getThis();
	}

	public B setSide(Enchantment e, int lv, int index) {
		recipe.side.get(index).input = new EnchantmentIngredient(e, lv);
		return getThis();
	}

	public B setSide(Ingredient in, ItemStack out, int index) {
		recipe.side.get(index).input = in;
		recipe.side.get(index).output = out;
		return getThis();
	}

	public B setSides(Ingredient in, ItemStack out, int... index) {
		for (int i : index) {
			setSide(in, out, i);
		}
		return getThis();
	}

	public B setSides(Item in, Item out, int... index) {
		return setSides(Ingredient.of(in), out.getDefaultInstance(), index);
	}

	public B setSides(Item item, int... index) {
		return setSides(Ingredient.of(item), ItemStack.EMPTY, index);
	}

}
