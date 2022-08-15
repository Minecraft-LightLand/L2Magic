package dev.xkmc.l2magic.content.common.particle;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EmeraldParticle extends TextureSheetParticle {

	public static final int LIFE = 20;

	protected EmeraldParticle(ClientLevel world, double x0, double y0, double z0, double x1, double y1, double z1) {
		super(world, x0, y0, z0, 0, 0, 0);
		this.xd = (x1 - x0) / LIFE;
		this.yd = (y1 - y0) / LIFE;
		this.zd = (z1 - z0) / LIFE;
		this.setSize(0.03F, 0.03F);
		this.gravity = 0;
		this.lifetime = LIFE;
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.lifetime-- <= 0) {
			this.remove();
		} else {
			this.move(this.xd, this.yd, this.zd);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleEngine.SpriteParticleRegistration<SimpleParticleType> {

		@Override
		public ParticleProvider<SimpleParticleType> create(SpriteSet sprite) {
			return (SimpleParticleType type, ClientLevel world, double x0, double y0, double z0, double x1, double y1, double z1) -> {
				EmeraldParticle part = new EmeraldParticle(world, x0, y0, z0, x1, y1, z1);
				part.pickSprite(sprite);
				return part;
			};
		}

	}
}
