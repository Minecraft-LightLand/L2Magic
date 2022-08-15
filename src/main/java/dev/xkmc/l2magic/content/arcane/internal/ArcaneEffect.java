package dev.xkmc.l2magic.content.arcane.internal;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.LLEffects;
import dev.xkmc.l2magic.init.special.ArcaneRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ArcaneEffect extends InherentEffect implements IconOverlayEffect {

	public static void addArcane(LivingEntity target, Entity source) {
		EffectUtil.addEffect(target, new MobEffectInstance(LLEffects.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), EffectUtil.AddReason.SKILL, source);
	}

	public ArcaneEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(L2Magic.MODID, "textures/effect_overlay/arcane.png");
	}
}
