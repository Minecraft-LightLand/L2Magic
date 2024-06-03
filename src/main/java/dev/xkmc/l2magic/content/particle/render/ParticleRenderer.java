package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraft.client.Camera;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ParticleRenderer {

	@OnlyIn(Dist.CLIENT)
	void onParticleInit(LMGenericParticle e);

	@OnlyIn(Dist.CLIENT)
	void onPostTick(LMGenericParticle e);

	RenderType renderType();

	@OnlyIn(Dist.CLIENT)
	boolean specialRender(LMGenericParticle lmGenericParticle, VertexConsumer vc, Camera camera, float pTick);

	enum RenderType {
		NORMAL, LIT, TRANSLUCENT, BLOCK
	}

}
