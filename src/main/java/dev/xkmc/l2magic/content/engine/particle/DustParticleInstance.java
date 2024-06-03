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
import dev.xkmc.l2magic.content.particle.render.DustParticleSprite;
import dev.xkmc.l2magic.content.particle.render.RandomColorParticle;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

public record DustParticleInstance(ColorVariable color, DoubleVariable scale, DoubleVariable speed)
		implements ParticleInstance<DustParticleInstance>, RandomColorParticle {

	public static final Codec<DustParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ColorVariable.CODEC.fieldOf("color").forGetter(e -> e.color),
			DoubleVariable.codec("scale", DustParticleInstance::scale),
			DoubleVariable.codec("speed", ParticleInstance::speed)
	).apply(i, DustParticleInstance::new));

	@Override
	public EngineType<DustParticleInstance> type() {
		return EngineRegistry.DUST_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		return new LMGenericParticleOption(new ClientParticleData(40, true, (float) scale.eval(ctx),
				ctx, SimpleMotion.ZERO, new DustParticleSprite(randomizeColor(ctx.rand(), color.eval(ctx)))));
	}

}
