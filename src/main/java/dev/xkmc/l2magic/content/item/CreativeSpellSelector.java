package dev.xkmc.l2magic.content.item;

import dev.xkmc.l2itemselector.select.item.IItemSelector;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class CreativeSpellSelector extends IItemSelector {

	private static List<ResourceLocation> getSpells(RegistryAccess level) {
		return level.registryOrThrow(EngineRegistry.SPELL).holders()
				.sorted(Comparator.<Holder.Reference<SpellAction>>comparingInt(e -> e.get().order())
						.thenComparing(e -> e.key().location().toString()))
				.map(e -> e.key().location()).toList();
	}

	public CreativeSpellSelector(ResourceLocation id) {
		super(id);
	}

	private ItemStack cache = ItemStack.EMPTY;

	@Override
	public boolean test(ItemStack stack) {
		if (stack.is(LMItems.CREATIVE_WAND.get())) {
			cache = stack;
			return true;
		}
		return false;
	}

	@Override
	public int getIndex(Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		ItemStack stack;
		if (test(main)) {
			stack = main;
		} else if (test(off)) {
			stack = off;
		} else return 0;
		var list = getSpells(player.level().registryAccess());
		if (list.size() >= 9) return 4;
		var id = WandItem.getSpellId(player.level(), stack);
		if (id == null) return 0;
		int index = list.indexOf(id);
		if (index < 0) return 0;
		return index;
	}

	private List<ItemStack> getListGeneric(Function<ResourceLocation, ItemStack> func) {
		var level = Proxy.getWorld();
		assert level != null;
		var id = WandItem.getSpellId(level, cache);
		var ans = new ArrayList<ItemStack>();
		var list = getSpells(level.registryAccess());
		int index = id == null ? -1 : list.indexOf(id);
		if (list.size() < 9) {
			if (index < 0)
				ans.add(cache.copy());
			for (var e : list)
				ans.add(func.apply(e));
			return ans;
		}
		if (index < 0) {
			for (int i = 0; i < 4; i++)
				ans.add(func.apply(list.get(list.size() - 4 + i)));
			ans.add(cache.copy());
			for (int i = 0; i < 4; i++)
				ans.add(func.apply(list.get(i)));
			return ans;
		}
		for (int i = 0; i < 4; i++)
			ans.add(func.apply(list.get((index - 4 + i + list.size()) % list.size())));
		ans.add(func.apply(id));
		for (int i = 0; i < 4; i++)
			ans.add(func.apply(list.get((index + 1 + i) % list.size())));
		return ans;
	}

	@Override
	public List<ItemStack> getDisplayList() {
		var level = Proxy.getWorld();
		assert level != null;
		return getListGeneric(rl -> level.registryAccess().registryOrThrow(EngineRegistry.SPELL).get(rl).icon().getDefaultInstance()
				.setHoverName(Component.translatable(SpellAction.lang(rl))));
	}

	@Override
	public List<ItemStack> getList() {
		var level = Proxy.getWorld();
		assert level != null;
		return getListGeneric(rl -> WandItem.setSpell(LMItems.CREATIVE_WAND.asStack(), rl));
	}

}
