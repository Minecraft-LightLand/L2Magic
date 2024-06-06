package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.resources.ResourceLocation;

public record FixedParticleSprite(
		RenderTypePreset renderType,
		ResourceLocation particle,
		int age, int total
) implements VanillaParticleSprite {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		e.setSprite(spriteSet().get(age, total));
	}

	@Override
	public void onPostTick(LMGenericParticle e) {
	}

}
