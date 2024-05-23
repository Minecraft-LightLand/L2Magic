package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;

public record TransitionParticleInstance(ColorVariable start, ColorVariable end, DoubleVariable scale,
										 DoubleVariable speed)
		implements ParticleInstance<TransitionParticleInstance> {

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
		return new DustColorTransitionOptions(start.eval(ctx), end.eval(ctx), (float) scale.eval(ctx));
	}

}
