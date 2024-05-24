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
		var id = WandItem.getSpellId(player.level(), stack);
		if (id == null) return 0;
		var list = getSpells(player.level().registryAccess());
		int index = list.indexOf(id);
		if (index < 0) return 0;
		int n = Math.min(list.size(), 9);
		int start = Math.max(0, Math.min(index - 4, list.size() - n));
		return index - start;
	}

	private List<ItemStack> getListGeneric(Function<ResourceLocation, ItemStack> func) {
		var level = Proxy.getWorld();
		assert level != null;
		var id = WandItem.getSpellId(level, cache);
		var ans = new ArrayList<ItemStack>();
		var list = getSpells(level.registryAccess());
		int index = id == null ? -1 : list.indexOf(id);
		int n = 9;
		if (index < 0) {
			ans.add(cache.copy());
			n--;
		}
		n = Math.min(list.size(), n);
		int start = Math.max(0, Math.min(index - 4, list.size() - n));
		for (int i = 0; i < n; i++) {
			int ind = start + i;
			ResourceLocation rl = list.get(ind);
			ans.add(func.apply(rl));
		}
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
