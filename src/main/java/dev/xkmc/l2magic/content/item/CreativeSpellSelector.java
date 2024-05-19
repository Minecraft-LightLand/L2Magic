package dev.xkmc.l2magic.content.item;

import dev.xkmc.l2itemselector.select.item.IItemSelector;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreativeSpellSelector extends IItemSelector {

	private static List<ResourceLocation> getSpells(RegistryAccess level) {
		return level.registryOrThrow(EngineRegistry.SPELL).keySet().stream()
				.sorted(Comparator.comparing(ResourceLocation::toString))
				.toList();
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
		if (index < 5) return index;
		if (list.size() - index <= 5) {
			return index + 10 - list.size();
		}
		return 4;
	}

	@Override
	public List<ItemStack> getDisplayList() {
		var level = Proxy.getWorld();
		assert level != null;
		var id = WandItem.getSpellId(level, cache);
		var ans = new ArrayList<ItemStack>();
		var list = getSpells(level.registryAccess());
		int index = id == null ? -1 : list.indexOf(id);
		if (index < 0) {
			ans.add(cache);
		}
		for (int i = -4; i <= 4; i++) {
			int ind = index + i;
			if (ind < 0 || ind >= list.size())
				continue;
			ResourceLocation rl = list.get(ind);
			var spell = level.registryAccess().registryOrThrow(EngineRegistry.SPELL).get(rl);
			if (spell == null)
				continue;
			ans.add(spell.icon().getDefaultInstance().setHoverName(Component.literal(list.get(ind).toString())));
		}
		return ans;
	}

	@Override
	public List<ItemStack> getList() {
		var level = Proxy.getWorld();
		assert level != null;
		var id = WandItem.getSpellId(level, cache);
		var ans = new ArrayList<ItemStack>();
		var list = getSpells(level.registryAccess());
		int index = id == null ? -1 : list.indexOf(id);
		if (index < 0) {
			ans.add(cache);
		}
		for (int i = -4; i <= 4; i++) {
			int ind = index + i;
			if (ind < 0 || ind >= list.size())
				continue;
			ans.add(WandItem.setSpell(LMItems.CREATIVE_WAND.asStack(), list.get(ind)));
		}
		return ans;
	}

}
