package dev.xkmc.l2magic.content.particle.render;

import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ParticleRenderer {

	@OnlyIn(Dist.CLIENT)
	void onParticleInit(LMGenericParticle e);

	@OnlyIn(Dist.CLIENT)
	void onPostTick(LMGenericParticle e);

	RenderType renderType();

	enum RenderType {
		NORMAL, LIT, TRANSLUCENT
	}
}
