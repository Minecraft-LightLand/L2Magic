package dev.xkmc.l2magic.content.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class LMGenericParticle extends TextureSheetParticle {

	protected LMGenericParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
		super(level, x, y, z, vx, vy, vz);
		//TODO
	}

	@Override
	public ParticleRenderType getRenderType() {
		return null;//TODO
	}

}
