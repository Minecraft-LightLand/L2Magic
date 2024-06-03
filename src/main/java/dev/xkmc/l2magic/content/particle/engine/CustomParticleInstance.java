package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.particle.ParticleInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

public record CustomParticleInstance(
		DoubleVariable speed, DoubleVariable scale, IntVariable life, boolean collide,
		Motion<?> motion, ParticleRenderData<?> renderer
		) implements ParticleInstance<CustomParticleInstance> {

	public static final Codec<CustomParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("speed", ParticleInstance::speed),
			DoubleVariable.codec("scale", e -> e.scale),
			IntVariable.codec("life", e -> e.life),
			Codec.BOOL.fieldOf("collide").forGetter(e -> e.collide),
			Motion.CODEC.fieldOf("motion").forGetter(e -> e.motion),
			ParticleRenderData.CODEC.fieldOf("renderer").forGetter(e -> e.renderer)
	).apply(i, CustomParticleInstance::new));

	@Override
	public EngineType<CustomParticleInstance> type() {
		return EngineRegistry.CUSTOM_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(
				life.eval(ctx), collide, (float) scale.eval(ctx), ctx,
				motion, renderer.resolve()
		));
	}

}
