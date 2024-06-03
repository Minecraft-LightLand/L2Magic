package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface SpriteData extends ParticleRenderer {

	@OnlyIn(Dist.CLIENT)
	SpriteSet spriteSet();

	@OnlyIn(Dist.CLIENT)
	@Override
	default void onParticleInit(LMGenericParticle e) {
		e.setSprite(spriteSet().get(0, 1));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	default void onPostTick(LMGenericParticle e) {
		e.setSpriteFromAge(spriteSet());
	}

	@Override
	default boolean specialRender(LMGenericParticle e, VertexConsumer vc, Camera camera, float pTick) {
		return false;
	}

}
