package dev.xkmc.l2magic.init.special;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2magic.content.magic.spell.EvokerFangSpell;
import dev.xkmc.l2magic.content.magic.spell.FireArrowSpell;
import dev.xkmc.l2magic.content.magic.spell.WaterTrapSpell;
import dev.xkmc.l2magic.content.magic.spell.WindBladeSpell;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.init.L2Magic;

public class SpellRegistry {

	public static final RegistryEntry<WindBladeSpell> BLADE_SIDE = reg("blade_side", WindBladeSpell::new);
	public static final RegistryEntry<WindBladeSpell> BLADE_FRONT = reg("blade_front", WindBladeSpell::new);
	public static final RegistryEntry<EvokerFangSpell> FANG_ROUND = reg("fang_round", EvokerFangSpell::new);
	public static final RegistryEntry<WaterTrapSpell> WATER_TRAP_0 = reg("water_trap_0", WaterTrapSpell::new);
	public static final RegistryEntry<WaterTrapSpell> WATER_TRAP_1 = reg("water_trap_1", WaterTrapSpell::new);
	public static final RegistryEntry<FireArrowSpell> FIRE_RAIN = reg("fire_rain", FireArrowSpell::new);
	public static final RegistryEntry<FireArrowSpell> EXPLOSION_RAIN = reg("explosion_rain", FireArrowSpell::new);
	public static final RegistryEntry<FireArrowSpell> FIRE_EXPLOSION = reg("fire_explosion", FireArrowSpell::new);

	public static <T extends Spell<?, ?>> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup) {
		return L2Magic.REGISTRATE.generic(LightLandRegistry.SPELL, id, sup).defaultLang().register();
	}

	public static void register() {
	}

}
