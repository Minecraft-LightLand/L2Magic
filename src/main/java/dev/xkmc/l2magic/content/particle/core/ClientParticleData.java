package dev.xkmc.l2magic.content.particle.core;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.SimpleParticleSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ClientParticleData(
		int life, boolean doCollision, float size,
		EngineContext ctx, Motion<?> motion, ParticleRenderer renderer
) implements LMParticleData {

	public static final LMParticleData DEFAULT = new ClientParticleData(
			40, false, 0.15f, null, SimpleMotion.ZERO,
			new SimpleParticleSprite(
					RenderTypePreset.LIT,
					new ResourceLocation("flame")
			));

	public static float randSize(EngineContext ctx) {
		return 0.1F * (ctx.rand().nextFloat() * 0.5F + 0.5F) * 2.0F;
	}

	public ProjectileMovement move(int age, Vec3 velocity, Vec3 position) {
		return motion.move(ctx.withParam("TickCount", age), velocity, position);
	}

}
