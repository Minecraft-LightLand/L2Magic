package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import net.minecraft.client.particle.SpriteSet;

public interface SpriteData extends ParticleRenderer {

	SpriteSet spriteSet();

	@Override
	default void onParticleInit(LMGenericParticle e) {
		e.setSprite(spriteSet().get(0,1));
	}

	@Override
	default void onPostTick(LMGenericParticle e) {
		e.setSpriteFromAge(spriteSet());
	}

}
