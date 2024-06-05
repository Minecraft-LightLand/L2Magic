package dev.xkmc.l2magic.content.engine.motion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.List;

public record MovePosMotion(
		List<Modifier<?>> modifiers
) implements SetPosMotion<MovePosMotion> {

	public static final Codec<MovePosMotion> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Modifier.CODEC).fieldOf("modifiers").forGetter(e -> e.modifiers)
	).apply(i, MovePosMotion::new));

	@Override
	public MotionType<MovePosMotion> type() {
		return EngineRegistry.MOVE_MOTION.get();
	}

	@Override
	public LocationContext move(EngineContext ctx) {
		for (var e : modifiers) {
			ctx = ctx.with(e.modify(ctx));
		}
		return ctx.loc();
	}

}
