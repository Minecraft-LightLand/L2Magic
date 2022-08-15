package dev.xkmc.l2magic.init.registrate;


import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2magic.content.common.effect.ArmorReduceEffect;
import dev.xkmc.l2magic.content.common.effect.CleanseEffect;
import dev.xkmc.l2magic.content.common.effect.DispellEffect;
import dev.xkmc.l2magic.content.common.effect.EmeraldPopeEffect;
import dev.xkmc.l2magic.content.common.effect.force.*;
import dev.xkmc.l2magic.content.common.effect.skill.*;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * handles enchantment, mob effects, and potions
 */
public class LLEffects {

	public static final RegistryEntry<ArcaneEffect> ARCANE = genEffect("arcane", () -> new ArcaneEffect(MobEffectCategory.NEUTRAL, 0x4800FF));
	public static final RegistryEntry<WaterTrapEffect> WATER_TRAP = genEffect("water_trap", () -> new WaterTrapEffect(MobEffectCategory.HARMFUL, 0x00007f));
	public static final RegistryEntry<HeavyEffect> HEAVY = genEffect("heavy", () -> new HeavyEffect(MobEffectCategory.HARMFUL, 0x404040));
	public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", () -> new FlameEffect(MobEffectCategory.HARMFUL, 0xFF0000));

	public static final RegistryEntry<EmeraldPopeEffect> EMERALD = genEffect("emerald", () -> new EmeraldPopeEffect(MobEffectCategory.NEUTRAL, 0x00FF00));
	public static final RegistryEntry<IceEffect> ICE = genEffect("frozen", () -> new IceEffect(MobEffectCategory.HARMFUL, 0x7f7fff));
	public static final RegistryEntry<ArmorReduceEffect> ARMOR_REDUCE = genEffect("armor_reduce", () -> new ArmorReduceEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

	public static final RegistryEntry<NoKnockBackEffect> NO_KB = genEffect("no_knockback", () -> new NoKnockBackEffect(MobEffectCategory.BENEFICIAL, 0xafafaf));
	public static final RegistryEntry<BloodThurstEffect> BLOOD_THURST = genEffect("blood_thirst", () -> new BloodThurstEffect(MobEffectCategory.BENEFICIAL, 0xffafaf));
	public static final RegistryEntry<ArmorBreakerEffect> ARMOR_BREAKER = genEffect("armor_breaker", () -> new ArmorBreakerEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

	public static final RegistryEntry<MobEffect> RUN_BOW = genEffect("run_bow", () -> new RunBowEffect(MobEffectCategory.BENEFICIAL, 0xffffff));
	public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));

	public static final RegistryEntry<CleanseEffect> CLEANSE = genEffect("cleanse", () -> new CleanseEffect(MobEffectCategory.NEUTRAL, 0xffffff));
	public static final RegistryEntry<DispellEffect> DISPELL = genEffect("dispell", () -> new DispellEffect(MobEffectCategory.NEUTRAL, 0x9f9f9f));


	public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList<>();

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return LightLand.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(LightLand.REGISTRATE, LightLand.REGISTRATE, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
		RegistryEntry<T> ans = LightLand.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(LightLand.REGISTRATE, LightLand.REGISTRATE, name, cb, ForgeRegistries.Keys.POTIONS, sup)).register();
		POTION_LIST.add(ans);
		return ans;
	}

	public static void register() {

	}

	public static void registerBrewingRecipe() {
	}

}
