package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.particle.ParticleInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.engine.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

import java.util.Optional;
import java.util.Set;

public record CustomParticleInstance(
		DoubleVariable speed, DoubleVariable scale, IntVariable life, boolean collide,
		Motion<?> motion, ParticleRenderData<?> renderer
) implements ParticleInstance<CustomParticleInstance> {

	public static final Codec<CustomParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("speed", ParticleInstance::speed),
			DoubleVariable.optionalCodec("scale", e -> e.scale),
			IntVariable.optionalCodec("life", e -> e.life),
			Codec.BOOL.optionalFieldOf("collide").forGetter(e -> Optional.of(e.collide)),
			Motion.CODEC.optionalFieldOf("motion").forGetter(e -> Optional.of(e.motion)),
			ParticleRenderData.CODEC.fieldOf("renderer").forGetter(e -> e.renderer)
	).apply(i, (a, b, c, d, e, f) -> new CustomParticleInstance(
			a, b.orElse(DoubleVariable.of("1")),
			c.orElse(IntVariable.of("40/rand(1,10)")),
			d.orElse(true),
			e.orElse(SimpleMotion.DUST), f)));

	@Override
	public EngineType<CustomParticleInstance> type() {
		return EngineRegistry.CUSTOM_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(
				life.eval(ctx), collide, (float) scale.eval(ctx), ctx,
				motion, renderer.resolve(ctx)
		));
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		speed.verify(ctx.of("speed"));
		scale.verify(ctx.of("scale"));
		life.verify(ctx.of("life"));
		motion.verify(ctx.of("motion", Set.of("TickCount")));
		renderer.verify(ctx.of("renderer"));
		return true;
	}
}
