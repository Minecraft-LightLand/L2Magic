package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.client.Camera;

public interface ModelSpriteData extends ParticleRenderer {

	@Override
	default void onPostTick(LMGenericParticle e) {

	}

	@Override
	default boolean specialRender(LMGenericParticle lmGenericParticle, VertexConsumer vc, Camera camera, float pTick) {
		return false;
	}

}
