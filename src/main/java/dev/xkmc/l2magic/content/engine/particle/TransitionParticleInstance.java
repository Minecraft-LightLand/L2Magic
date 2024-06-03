package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.content.particle.render.RandomColorParticle;
import dev.xkmc.l2magic.content.particle.render.TransitionParticleSprite;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

public record TransitionParticleInstance(ColorVariable start, ColorVariable end, DoubleVariable scale,
										 DoubleVariable speed)
		implements ParticleInstance<TransitionParticleInstance>, RandomColorParticle {

	public static final Codec<TransitionParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ColorVariable.CODEC.fieldOf("start").forGetter(e -> e.start),
			ColorVariable.CODEC.fieldOf("end").forGetter(e -> e.end),
			DoubleVariable.codec("scale", TransitionParticleInstance::scale),
			DoubleVariable.codec("speed", ParticleInstance::speed)
	).apply(i, TransitionParticleInstance::new));

	@Override
	public EngineType<TransitionParticleInstance> type() {
		return EngineRegistry.TRANSITION_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(40, true, (float) scale.eval(ctx),
				ctx, SimpleMotion.ZERO, new TransitionParticleSprite(
				randomizeColor(ctx.rand(), start.eval(ctx)),
				randomizeColor(ctx.rand(), end.eval(ctx))
		)));
	}

}
