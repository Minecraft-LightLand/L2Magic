package dev.xkmc.l2magic.content.common.effect.force;

import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2magic.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;

public class ArcaneEffect extends InherentEffect implements IconOverlayEffect {

	public ArcaneEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(LightLand.MODID, "textures/effect_overlay/arcane.png");
	}
}
