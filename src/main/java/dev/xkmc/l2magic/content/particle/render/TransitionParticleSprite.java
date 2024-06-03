package dev.xkmc.l2magic.content.particle.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import net.minecraft.client.Camera;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record TransitionParticleSprite(Vector3f start, Vector3f end) implements VanillaParticleSprite {

	private static final ResourceLocation ID = new ResourceLocation("dust_color_transition");

	@Override
	public RenderType renderType() {
		return RenderType.NORMAL;
	}

	@Override
	public ResourceLocation particle() {
		return ID;
	}

	@Override
	public void onParticleInit(LMGenericParticle e) {
		VanillaParticleSprite.super.onParticleInit(e);
		e.setColor(start.x, start.y, start.z);
	}

	@Override
	public boolean specialRender(LMGenericParticle e, VertexConsumer vc, Camera camera, float pTick) {
		float f = (e.age() + pTick) / (e.getLifetime() + 1);
		Vector3f col = new Vector3f(start).lerp(end, f);
		e.setColor(col.x, col.y, col.z);
		return false;
	}

}
