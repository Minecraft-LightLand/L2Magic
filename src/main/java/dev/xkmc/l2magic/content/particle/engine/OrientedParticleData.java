package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.particle.render.OrientedSpriteRenderer;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;

public record OrientedParticleData(
		ParticleRenderData<?> inner,
		DoubleVariable roll,
		boolean fixFacing
) implements ParticleRenderData<OrientedParticleData> {

	public static final Codec<OrientedParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
			ParticleRenderData.CODEC.fieldOf("inner").forGetter(e -> e.inner),
			DoubleVariable.optionalCodec("roll", e -> e.roll),
			Codec.BOOL.optionalFieldOf("fixFacing").forGetter(e -> Optional.of(e.fixFacing))
	).apply(i, (c, r, f) -> new OrientedParticleData(c, r.orElse(DoubleVariable.ZERO), f.orElse(false))));

	@Override
	public ParticleRenderType<OrientedParticleData> type() {
		return EngineRegistry.ORIENTED_RENDER.get();
	}

	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new OrientedSpriteRenderer(inner().resolve(ctx),
				fixFacing ? ctx.loc().dir() : Vec3.ZERO,
				t -> roll.eval(ctx.withParam("Age", t)));
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		return inner.verify(ctx.of("inner")) & roll.verify(ctx.of("roll", Set.of("Age")));
	}
}
