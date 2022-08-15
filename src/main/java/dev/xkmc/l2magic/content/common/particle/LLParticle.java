package dev.xkmc.l2magic.content.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;

public class LLParticle extends Particle {

	protected LLParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}

	public LLParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
		super(level, x, y, z, vx, vy, vz);
	}

	@Override
	public void render(VertexConsumer con, Camera camera, float pTick) {

	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

}
