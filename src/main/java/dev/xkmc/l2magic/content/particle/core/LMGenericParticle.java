package dev.xkmc.l2magic.content.particle.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class LMGenericParticle extends TextureSheetParticle {

	private final LMParticleData data;

	private SpriteGeom geom = SpriteGeom.INSTANCE;

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
		quadSize *= data.size();
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
	public void render(VertexConsumer vc, Camera camera, float pTick) {
		if (data.renderer().specialRender(this, vc, camera, pTick))
			return;
		super.render(vc, camera, pTick);
	}

	@Override
	protected float getU0() {
		return sprite.getU(geom.u0());
	}

	@Override
	protected float getU1() {
		return sprite.getU(geom.u1());
	}

	@Override
	protected float getV0() {
		return sprite.getV(geom.v0());
	}

	@Override
	protected float getV1() {
		return sprite.getV(geom.v1());
	}

	public RandomSource random() {
		return random;
	}

	public int age() {
		return age;
	}

	public ClientLevel level() {
		return level;
	}

	public void setGeom(SpriteGeom geom) {
		this.geom = geom;
	}

	private static final ParticleRenderType BLOCK_LIT = new ParticleRenderType() {
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		public void end(Tesselator tesselator) {
			tesselator.end();
		}

		public String toString() {
			return "TERRAIN_SHEET_LIT";
		}
	};

	@Override
	public ParticleRenderType getRenderType() {
		return switch (data.renderer().renderType()) {
			case NORMAL -> ParticleRenderType.PARTICLE_SHEET_OPAQUE;
			case LIT -> ParticleRenderType.PARTICLE_SHEET_LIT;
			case TRANSLUCENT -> ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
			case BLOCK -> ParticleRenderType.TERRAIN_SHEET;
			case BLOCK_LIT -> BLOCK_LIT;
		};
	}

}
