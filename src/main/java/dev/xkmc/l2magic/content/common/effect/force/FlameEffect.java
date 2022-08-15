package dev.xkmc.l2magic.content.common.effect.force;

import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.util.DamageUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FlameEffect extends InherentEffect implements IconOverlayEffect {

	public FlameEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public void applyEffectTick(LivingEntity target, int level) {
		DamageUtil.dealDamage(target, DamageSource.IN_FIRE, 2 << level);
	}

	@Override
	public boolean isDurationEffectTick(int tick, int level) {
		return tick % 20 == 0;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(L2Magic.MODID, "textures/effect_overlay/flame.png");
	}
}
