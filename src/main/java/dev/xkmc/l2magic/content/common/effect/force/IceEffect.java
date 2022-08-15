package dev.xkmc.l2magic.content.common.effect.force;

import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class IceEffect extends InherentEffect implements IconOverlayEffect {

	public IceEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public void applyEffectTick(LivingEntity target, int level) {
		target.setIsInPowderSnow(true);
	}

	@Override
	public boolean isDurationEffectTick(int tick, int level) {
		return true;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(L2Magic.MODID, "textures/effect_overlay/flame.png");
	}
}
