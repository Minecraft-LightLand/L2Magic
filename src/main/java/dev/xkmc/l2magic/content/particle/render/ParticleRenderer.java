package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;

public interface ParticleRenderer {

	void onParticleInit(LMGenericParticle e);

	void onPostTick(LMGenericParticle e);

}
