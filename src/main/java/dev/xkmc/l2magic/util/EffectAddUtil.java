package dev.xkmc.l2magic.util;

import dev.xkmc.l2library.base.effects.EffectSyncEvents;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2magic.init.registrate.LLEffects;
import dev.xkmc.l2magic.init.special.ArcaneRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectAddUtil {

	public static void addArcane(LivingEntity target, Entity source) {
		EffectUtil.addEffect(target, new MobEffectInstance(LLEffects.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), EffectUtil.AddReason.SKILL, source);
	}

	public static void init() {
		EffectSyncEvents.TRACKED.add(LLEffects.ARCANE.get());
		EffectSyncEvents.TRACKED.add(LLEffects.WATER_TRAP.get());
		EffectSyncEvents.TRACKED.add(LLEffects.FLAME.get());
		EffectSyncEvents.TRACKED.add(LLEffects.EMERALD.get());
		EffectSyncEvents.TRACKED.add(LLEffects.ICE.get());
	}

}
