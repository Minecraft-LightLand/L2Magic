package dev.xkmc.l2magic.content.magic.ritual;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.magic.block.RitualCore;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SerialClass
public class AbstractRitualRecipe<R extends AbstractRitualRecipe<R>> extends BaseRecipe<R, AbstractRitualRecipe<?>, RitualCore.Inv> {

	@SerialClass.SerialField
	public Entry core;

	@SerialClass.SerialField
	public ArrayList<Entry> side;

	public AbstractRitualRecipe(ResourceLocation id, RecType<R, AbstractRitualRecipe<?>, RitualCore.Inv> fac) {
		super(id, fac);
	}

	@Override
	public boolean matches(RitualCore.Inv inv, Level world) {
		if (!core.test(inv.getItem(5)))
			return false;
		List<Entry> temp = side.stream().filter(e -> !e.input.isEmpty()).collect(Collectors.toList());
		for (int i = 0; i < 9; i++) {
			if (i == 5)
				continue;
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				Optional<Entry> entry = temp.stream().filter(e -> e.test(stack)).findFirst();
				if (entry.isEmpty())
					return false;
				temp.remove(entry.get());
			}
		}
		return temp.isEmpty();
	}

	@Deprecated
	@Override
	public ItemStack assemble(RitualCore.Inv inv) {
		if (!core.test(inv.getItem(5)))
			return ItemStack.EMPTY;
		ItemStack ans = core.output.copy();
		List<Entry> temp = side.stream().filter(e -> !e.input.isEmpty()).collect(Collectors.toList());
		for (int i = 0; i < 9; i++) {
			if (i == 5)
				continue;
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				Optional<Entry> entry = temp.stream().filter(e -> e.test(stack)).findFirst();
				if (!entry.isPresent())
					return ItemStack.EMPTY;
				temp.remove(entry.get());
				inv.setItem(i, entry.get().getOutput(stack));
			}
		}
		return ans;
	}

	public void assemble(RitualCore.Inv inv, int level) {
		inv.setItem(5, assemble(inv));
	}

	@Override
	public boolean canCraftInDimensions(int r, int c) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return core.output;
	}

	@SerialClass
	public static class Entry {

		@SerialClass.SerialField
		public Ingredient input = Ingredient.EMPTY;

		@SerialClass.SerialField
		public ItemStack output = ItemStack.EMPTY;

		public boolean test(ItemStack stack) {
			return input.test(stack);
		}

		@SuppressWarnings("ConstantConditions")
		public ItemStack getOutput(ItemStack stack) {
			return output.copy();
		}

	}

	@Nullable
	public ResourceLocation getMagic() {
		return null;
	}

	public int getLevel(int cost) {
		return 1;
	}

	public int getNextLevel(int cost) {
		return 0;
	}

	@Nullable
	private static Pair<Item, Integer> aggregate(NonNullList<ItemStack> list) {
		Item item = null;
		int count = 0;
		for (ItemStack stack_b : list) {
			if (stack_b.isEmpty()) {
				continue;
			}
			if (item == null) {
				item = stack_b.getItem();
				count = stack_b.getCount();
			} else {
				if (item != stack_b.getItem()) {
					return null;
				}
				count += stack_b.getCount();
			}
		}
		return Pair.of(item, count);
	}

	@SuppressWarnings("deprecation")
	public static NonNullList<ItemStack> fill(Item item, int count) {
		NonNullList<ItemStack> list = NonNullList.withSize(27, ItemStack.EMPTY);
		for (int i = 0; i < 27; i++) {
			if (count == 0)
				break;
			int c = Math.min(count, item.getMaxStackSize());
			list.set(i, new ItemStack(item, c));
			count -= c;
		}
		return list;
	}

}
