package dev.xkmc.l2magic.init.registrate;


import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneEffect;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * handles enchantment, mob effects, and potions
 */
public class LLEffects {

	public static final RegistryEntry<ArcaneEffect> ARCANE = genEffect("arcane", () -> new ArcaneEffect(MobEffectCategory.NEUTRAL, 0x4800FF));

	public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList<>();

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return L2Magic.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(L2Magic.REGISTRATE, L2Magic.REGISTRATE, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
		RegistryEntry<T> ans = L2Magic.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(L2Magic.REGISTRATE, L2Magic.REGISTRATE, name, cb, ForgeRegistries.Keys.POTIONS, sup)).register();
		POTION_LIST.add(ans);
		return ans;
	}

	public static void register() {

	}

	public static void registerBrewingRecipe() {
	}

}
