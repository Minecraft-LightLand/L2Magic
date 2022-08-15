package dev.xkmc.l2magic.init.special;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.magic.products.MagicElement;
import dev.xkmc.l2magic.content.magic.products.MagicProductType;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2magic.init.L2Magic.REGISTRATE;

public class LightLandRegistry {

	public static final L2Registrate.RegistryInstance<MagicElement> ELEMENT = REGISTRATE.newRegistry("magic_element", MagicElement.class);
	public static final L2Registrate.RegistryInstance<MagicProductType<?, ?>> PRODUCT_TYPE = REGISTRATE.newRegistry("magic_product_type", MagicProductType.class);
	public static final L2Registrate.RegistryInstance<ArcaneType> ARCANE_TYPE = REGISTRATE.newRegistry("arcane_type", ArcaneType.class);
	public static final L2Registrate.RegistryInstance<Arcane> ARCANE = REGISTRATE.newRegistry("arcane", Arcane.class);
	public static final L2Registrate.RegistryInstance<Spell<?, ?>> SPELL = REGISTRATE.newRegistry("spell", Spell.class);

	public static RegistryEntry<Attribute> SPELL_BOOST = REGISTRATE.simple("spell_boost", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.spell_boost", 1, 0, 1000).setSyncable(true));
	public static RegistryEntry<Attribute> MAX_MANA = REGISTRATE.simple("max_mana", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.max_mana", 0, 0, 1000000).setSyncable(true));
	public static RegistryEntry<Attribute> MAX_SPELL_LOAD = REGISTRATE.simple("max_spell_load", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.max_spell_load", 100, 100, 1000000).setSyncable(true));
	public static RegistryEntry<Attribute> MANA_RESTORE = REGISTRATE.simple("mana_restore", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () -> new RangedAttribute("attribute.name.mana_restore", 0.01, 0, 1).setSyncable(true));

	public static void register() {

	}

}
