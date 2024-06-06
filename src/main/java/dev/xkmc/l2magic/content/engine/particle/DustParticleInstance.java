package dev.xkmc.l2magic.content.engine.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.core.ClientParticleData;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticleOption;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.DustParticleSprite;
import dev.xkmc.l2magic.content.particle.render.RandomColorParticle;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleOptions;

public record DustParticleInstance(
		ColorVariable color,
		DoubleVariable scale,
		DoubleVariable speed,
		IntVariable life
) implements ParticleInstance<DustParticleInstance>, RandomColorParticle {

	public static final Codec<DustParticleInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			ColorVariable.CODEC.fieldOf("color").forGetter(e -> e.color),
			DoubleVariable.codec("scale", DustParticleInstance::scale),
			DoubleVariable.codec("speed", ParticleInstance::speed),
			IntVariable.codec("life", e -> e.life)
	).apply(i, DustParticleInstance::new));

	@Override
	public EngineType<DustParticleInstance> type() {
		return EngineRegistry.DUST_PARTICLE.get();
	}

	@Override
	public ParticleOptions particle(EngineContext ctx) {
		int life = life().eval(ctx);
		float scale = (float) scale().eval(ctx) * ClientParticleData.randSize(ctx);
		return new LMGenericParticleOption(new ClientParticleData(life, true, scale,
				ctx, SimpleMotion.DUST, new DustParticleSprite(RenderTypePreset.NORMAL,
				randomizeColor(ctx.rand(), color.eval(ctx)))));
	}

}
