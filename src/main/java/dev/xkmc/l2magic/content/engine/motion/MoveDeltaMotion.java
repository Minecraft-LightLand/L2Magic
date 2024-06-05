package dev.xkmc.l2magic.content.engine.motion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.List;

public record MoveDeltaMotion(
		List<Modifier<?>> modifiers
) implements SetDeltaMotion<MoveDeltaMotion> {

	public static final Codec<MoveDeltaMotion> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Modifier.CODEC).fieldOf("modifiers").forGetter(e -> e.modifiers)
	).apply(i, MoveDeltaMotion::new));

	@Override
	public MotionType<MoveDeltaMotion> type() {
		return EngineRegistry.DELTA_MOTION.get();
	}

	@Override
	public LocationContext move(EngineContext ctx) {
		for (var e : modifiers) {
			ctx = ctx.with(e.modify(ctx));
		}
		return ctx.loc();
	}

}
