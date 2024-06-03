package dev.xkmc.l2magic.content.particle.core;

import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.VanillaParticleSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ClientParticleData(
		int life, boolean doCollision,
		EngineContext ctx, Motion<?> motion, ParticleRenderer renderer
) implements LMParticleData {

	public static final LMParticleData DEFAULT = new ClientParticleData(
			40, false, null, SimpleMotion.ZERO,
			new VanillaParticleSprite(
					ParticleRenderer.RenderType.LIT,
					new ResourceLocation("flame")
			));

	public Vec3 move(int age, Vec3 velocity, Vec3 position) {
		return motion.move(ctx, velocity, position).vec();
	}

}
