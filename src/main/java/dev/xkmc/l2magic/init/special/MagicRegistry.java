package dev.xkmc.l2magic.init.special;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2magic.compat.api.MagicBehaviorListener;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.products.instance.*;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2magic.init.L2Magic.REGISTRATE;

public class MagicRegistry {

	public static final L2Registrate.RegistryInstance<MagicElement> ELEMENT = REGISTRATE.newRegistry("magic_element", MagicElement.class);
	public static final L2Registrate.RegistryInstance<MagicProductType<?, ?>> PRODUCT_TYPE = REGISTRATE.newRegistry("magic_product_type", MagicProductType.class);
	public static final L2Registrate.RegistryInstance<ArcaneType> ARCANE_TYPE = REGISTRATE.newRegistry("arcane_type", ArcaneType.class);

	public static final RegistryEntry<Attribute> SPELL_BOOST = REGISTRATE.simple("spell_boost", ForgeRegistries.ATTRIBUTES.getRegistryKey(),
			() -> new RangedAttribute("attribute.name.spell_boost", 1, 0, 1000).setSyncable(true));
	public static final RegistryEntry<Attribute> MAX_MANA = REGISTRATE.simple("max_mana", ForgeRegistries.ATTRIBUTES.getRegistryKey(),
			() -> new RangedAttribute("attribute.name.max_mana", MagicBehaviorListener.INSTANCE.getDefaultMana(), 0, 1000000).setSyncable(true));
	public static final RegistryEntry<Attribute> MAX_SPELL_LOAD = REGISTRATE.simple("max_spell_load", ForgeRegistries.ATTRIBUTES.getRegistryKey(),
			() -> new RangedAttribute("attribute.name.max_spell_load", MagicBehaviorListener.INSTANCE.getDefaultLoad(), 100, 1000000).setSyncable(true));
	public static final RegistryEntry<Attribute> MANA_RESTORE = REGISTRATE.simple("mana_restore", ForgeRegistries.ATTRIBUTES.getRegistryKey(),
			() -> new RangedAttribute("attribute.name.mana_restore", MagicBehaviorListener.INSTANCE.getDefaultManaRestore(), 0, 1).setSyncable(true));
	public static final RegistryEntry<Attribute> LOAD_RESTORE = REGISTRATE.simple("load_restore", ForgeRegistries.ATTRIBUTES.getRegistryKey(),
			() -> new RangedAttribute("attribute.name.load_restore", MagicBehaviorListener.INSTANCE.getDefaultLoadRestore(), 0, 1).setSyncable(true));

	public static final RegistryEntry<MagicElement> ELEM_EARTH = regElem("earth", () -> new MagicElement(0x7F511F));
	public static final RegistryEntry<MagicElement> ELEM_WATER = regElem("water", () -> new MagicElement(0x008ED6));
	public static final RegistryEntry<MagicElement> ELEM_AIR = regElem("air", () -> new MagicElement(0xB2FF6B));
	public static final RegistryEntry<MagicElement> ELEM_FIRE = regElem("fire", () -> new MagicElement(0xFF3B21));
	public static final RegistryEntry<MagicElement> ELEM_QUINT = regElem("quint", () -> new MagicElement(0x9400FF));

	public static final RegistryEntry<MagicProductType<Enchantment, EnchantmentMagic>> MPT_ENCH =
			regProd("enchantment", () -> new MagicProductType<>(EnchantmentMagic.class, EnchantmentMagic::new,
					() -> ForgeRegistries.ENCHANTMENTS, Enchantment::getDescriptionId, ELEM_AIR.get()));
	public static final RegistryEntry<MagicProductType<MobEffect, PotionMagic>> MPT_EFF =
			regProd("effect", () -> new MagicProductType<>(PotionMagic.class, PotionMagic::new,
					() -> ForgeRegistries.MOB_EFFECTS, MobEffect::getDescriptionId, ELEM_WATER.get()));
	public static final L2Registrate.RegistryInstance<Arcane> ARCANE = REGISTRATE.newRegistry("arcane", Arcane.class);
	public static final RegistryEntry<MagicProductType<Arcane, ArcaneMagic>> MPT_ARCANE =
			regProd("arcane", () -> new MagicProductType<>(ArcaneMagic.class, ArcaneMagic::new,
					ARCANE, Arcane::getDescriptionId, ELEM_QUINT.get()));
	public static final L2Registrate.RegistryInstance<Spell<?, ?>> SPELL = REGISTRATE.newRegistry("spell", Spell.class);
	public static final RegistryEntry<MagicProductType<Spell<?, ?>, SpellMagic>> MPT_SPELL =
			regProd("spell", () -> new MagicProductType<>(SpellMagic.class, SpellMagic::new,
					SPELL, Spell::getDescriptionId, ELEM_FIRE.get()));
	public static final RegistryEntry<MagicProductType<Item, CraftMagic>> MPT_CRAFT =
			regProd("craft", () -> new MagicProductType<>(CraftMagic.class, CraftMagic::new,
					() -> ForgeRegistries.ITEMS, Item::getDescriptionId, ELEM_EARTH.get()));

	public static <T extends MagicProductType<?, ?>> RegistryEntry<T> regProd(String id, NonNullSupplier<T> sup) {
		return L2Magic.REGISTRATE.generic(PRODUCT_TYPE, id, sup).defaultLang().register();
	}

	public static <T extends MagicElement> RegistryEntry<T> regElem(String id, NonNullSupplier<T> sup) {
		return L2Magic.REGISTRATE.generic(ELEMENT, id, sup).defaultLang().register();
	}

	public static RegistryEntry<Attribute> regAttribute(String name, NonNullSupplier<Attribute> sup) {
		return L2Magic.REGISTRATE.simple(name, ForgeRegistries.Keys.ATTRIBUTES, sup);
	}

	public static void register() {
	}

}
