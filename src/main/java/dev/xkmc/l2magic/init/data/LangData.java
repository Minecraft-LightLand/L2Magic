package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2library.idea.magic.HexDirection;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.magic.gui.craft.ArcaneInjectContainer;
import dev.xkmc.l2magic.content.magic.gui.craft.SpellCraftContainer;
import dev.xkmc.l2magic.content.magic.gui.hex.HexStatus;
import dev.xkmc.l2magic.content.magic.products.info.ProductState;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.LLEffects;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class LangData {

	public enum IDS {
		INVALID_ID("argument.invalid_id", 0),
		LOCKED("chat.locked", 0),
		UNLOCKED("chat.unlocked", 0),
		GET_ARCANE_MANA("chat.show_arcane_mana", 2),
		SPELL_SLOT("chat.spell_slot", 1),
		ACTION_SUCCESS("chat.action_success", 0),
		PLAYER_NOT_FOUND("chat.player_not_found", 0),
		WRONG_ITEM("chat.wrong_item", 0),
		TARGET_ALLY("tooltip.target.ally", 0),
		TARGET_ENEMY("tooltip.target.enemy", 0),
		TARGET_ALL("tooltip.target.all", 0),
		POTION_RADIUS("tooltip.potion.radius", 1),
		CONT_RITUAL("container.ritual", 0),
		MANA_COST("tooltip.mana_cost", 1),
		ENCH_LV("tooltip.enchantment_result.level", 1),
		ENCH_NEXT("tooltip.enchantment_result.next", 1),
		ENCH_ELEM("tooltip.enchantment_result.elements", 0),
		RITUAL_WRONG("chat.ritual.fail.wrong", 0),
		RITUAL_ZERO("chat.ritual.fail.zero", 0),
		RITUAL_ELEM("chat.ritual.fail.element", 0),


		HEX_COST("screen.hex.cost", 1),
		GUI_TREE_ELEM_PRE("screen.magic_tree.elem.require", 0),
		GUI_TREE_ELEM_POST("screen.magic_tree.elem.lv", 1),
		GUI_TREE_COST("screen.magic_tree.cost", 1),
		GUI_TREE_SHORT("screen.magic_tree.short", 0),
		GUI_TREE_REPEAT("screen.magic_tree.repeat", 0),
		GUI_ARCANE("screen.ability.arcane.title", 0),
		GUI_ARCANE_COST("screen.ability.arcane.cost", 2),
		GUI_ELEMENT("screen.ability.elemental.title", 0),
		GUI_ELEMENT_LV("screen.ability.elemental.desc.lv", 1),
		GUI_ELEMENT_COUNT("screen.ability.elemental.desc.count", 1),
		GUI_ELEMENT_COST("screen.ability.elemental.desc.cost", 2),

		GUI_SPELL_CRAFT_ELEM_COST("screen.spell_craft.elem_cost", 2);

		final String id;
		final int count;

		IDS(String id, int count) {
			this.id = id;
			this.count = count;
		}

		public MutableComponent get(Object... objs) {
			if (objs.length != count)
				throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
			return translate(L2Magic.MODID + "." + id, objs);
		}

	}

	public interface LangEnum<T extends Enum<T> & LangEnum<T>> {

		int getCount();

		@Nullable
		default Class<? extends Enum<?>> mux() {
			return null;
		}

		@SuppressWarnings({"unchecked"})
		default T getThis() {
			return (T) this;
		}

	}

	public static final Map<Class<? extends Enum<?>>, String> MAP = new HashMap<>();
	public static final Map<Class<? extends LangEnum<?>>, String> LANG_MAP = new HashMap<>();

	static {
		MAP.put(HexDirection.class, "screen.hex.direction.");
		MAP.put(HexStatus.Compile.class, "screen.hex.compile.");
		MAP.put(HexStatus.Save.class, "screen.hex.save.");
		MAP.put(ArcaneType.Hit.class, "screen.ability.arcane.activate.");
		MAP.put(ProductState.class, "screen.magic_tree.status.");

		LANG_MAP.put(SpellCraftContainer.Error.class, "screen.spell_craft.error.");
		LANG_MAP.put(ArcaneInjectContainer.Error.class, "screen.arcane_inject.error.");
	}

	public static MutableComponent get(Enum<?> obj, Object... args) {
		if (MAP.containsKey(obj.getClass()))
			return translate(L2Magic.MODID + "." + MAP.get(obj.getClass()) + obj.name().toLowerCase(Locale.ROOT), args);
		if (LANG_MAP.containsKey(obj.getClass())) {
			if (obj instanceof LangEnum<?> lang) {
				if (lang.mux() != null) {
					return translate(L2Magic.MODID + "." + LANG_MAP.get(obj.getClass()) +
							obj.name().toLowerCase(Locale.ROOT) + ((Enum<?>) args[0]).name().toLowerCase(Locale.ROOT));
				}
			}
			return translate(L2Magic.MODID + "." + LANG_MAP.get(obj.getClass()) + obj.name().toLowerCase(Locale.ROOT), args);
		}
		return translate("unknown.enum." + obj.name().toLowerCase(Locale.ROOT));
	}

	public static void addTranslations(BiConsumer<String, String> pvd) {
		for (IDS id : IDS.values()) {
			String[] strs = id.id.split("\\.");
			String str = strs[strs.length - 1];
			pvd.accept(L2Magic.MODID + "." + id.id,
					RegistrateLangProvider.toEnglishName(str) + getParams(id.count));
		}
		pvd.accept("itemGroup.l2magic.material", "L2 Magic");
		MAP.forEach((v, k) -> {
			for (Enum<?> e : v.getEnumConstants()) {
				String en = e.name().toLowerCase(Locale.ROOT);
				pvd.accept(L2Magic.MODID + "." + k + en,
						RegistrateLangProvider.toEnglishName(en));
			}
		});
		LANG_MAP.forEach((v, k) -> {
			for (LangEnum<?> e : v.getEnumConstants()) {
				String en = e.getThis().name().toLowerCase(Locale.ROOT);
				Class<? extends Enum<?>> next = e.mux();
				if (next != null) {
					for (Enum<?> n : next.getEnumConstants()) {
						String ne = n.name().toLowerCase(Locale.ROOT);
						pvd.accept(L2Magic.MODID + "." + k + en + "." + ne,
								RegistrateLangProvider.toEnglishName(en) + " " + RegistrateLangProvider.toEnglishName(ne));
					}
				} else
					pvd.accept(L2Magic.MODID + "." + k + en,
							RegistrateLangProvider.toEnglishName(en) + getParams(e.getCount()));
			}
		});

		List<Item> list = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);
		for (RegistryEntry<? extends Potion> ent : LLEffects.POTION_LIST) {
			for (Item item : list) {
				String str = ent.get().getName(item.getDescriptionId() + ".effect.");
				pvd.accept(str, RegistrateLangProvider.toEnglishName(str));
			}
		}
		for (Lore lore : Lore.values()) {
			pvd.accept("lore." + L2Magic.MODID + "." + lore.id, lore.lore);
		}
	}

	private static String getParams(int count) {
		if (count <= 0)
			return "";
		StringBuilder pad = new StringBuilder();
		for (int i = 0; i < count; i++) {
			pad.append(pad.length() == 0 ? ": " : "/");
			pad.append("%s");
		}
		return pad.toString();
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent translate(String key, Object... objs) {
		return MutableComponent.create(new TranslatableContents(key, objs));
	}

}
