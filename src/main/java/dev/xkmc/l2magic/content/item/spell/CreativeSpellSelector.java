package dev.xkmc.l2magic.content.item.spell;

import dev.xkmc.l2itemselector.select.item.CircularSelector;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreativeSpellSelector extends CircularSelector<ResourceKey<SpellAction>> {

	public CreativeSpellSelector(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.is(LMItems.CREATIVE_WAND.get());
	}

	@Nullable
	@Override
	public ResourceKey<SpellAction> getSelected(ItemStack stack) {
		return WandItem.getSpellId(stack);
	}

	public List<ResourceKey<SpellAction>> getAll(ItemStack stack) {
		var reg = CommonHooks.resolveLookup(EngineRegistry.SPELL);
		if (reg == null) return List.of();
		var id = LMItems.MODID.getOrDefault(stack, L2Magic.MODID);
		return reg.listElementIds().filter(e -> e.location().getNamespace().equals(id)).toList();
	}

	@Override
	public List<ItemStack> getDisplayList(ItemStack stack) {
		var reg = CommonHooks.resolveLookup(EngineRegistry.SPELL);
		if (reg == null) return List.of();
		return getListGeneric(stack, rl -> Util.make(reg.getOrThrow(rl).value().icon().getDefaultInstance(),
				e -> e.set(DataComponents.CUSTOM_NAME, Component.translatable(SpellAction.lang(rl.location())))));
	}

	@Override
	public List<ItemStack> getList(ItemStack stack) {
		return getListGeneric(stack, rl -> WandItem.setSpell(stack.copy(), rl));
	}

	@Override
	public int getSelHash(ItemStack stack) {
		var obj = getSelected(stack);
		if (obj == null) return 0;
		return obj.location().toString().hashCode();
	}

}
