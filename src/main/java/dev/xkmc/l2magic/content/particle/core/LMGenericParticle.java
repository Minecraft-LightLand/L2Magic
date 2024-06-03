package dev.xkmc.l2magic.content.particle.core;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public class LMGenericParticle extends TextureSheetParticle {

	private final LMParticleData data;

	public LMGenericParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, LMParticleData data) {
		super(level, x, y, z, vx, vy, vz);
		this.x = xo = x;
		this.y = yo = y;
		this.z = zo = z;
		xd = vx;
		yd = vy;
		zd = vz;
		gravity = 0;
		friction = 0;
		lifetime = data.life();
		hasPhysics = data.doCollision();
		this.data = data;
		data.renderer().onParticleInit(this);
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		Vec3 speed = data.move(age, new Vec3(xd, yd, zd), new Vec3(x, y, z));
		if (age++ >= lifetime) {
			remove();
			return;
		}
		xd = speed.x;
		yd = speed.y;
		zd = speed.z;
		move(xd, yd, zd);

		if (speedUpWhenYMotionIsBlocked && y == yo) {
			xd *= 1.1D;
			zd *= 1.1D;
		}

		if (onGround) {
			xd *= 0.7F;
			zd *= 0.7F;
		}
		data.renderer().onPostTick(this);
	}

	@Override
	public void setSprite(TextureAtlasSprite atlas) {
		super.setSprite(atlas);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return switch (data.renderer().renderType()) {
			case NORMAL -> ParticleRenderType.PARTICLE_SHEET_OPAQUE;
			case LIT -> ParticleRenderType.PARTICLE_SHEET_LIT;
			case TRANSLUCENT -> ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
		};
	}

}
