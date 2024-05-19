package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record ForwardOffsetModifier(DoubleVariable distance, ConfiguredEngine<?> child)
		implements Modifier<ForwardOffsetModifier> {

	public static Codec<ForwardOffsetModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("distance", e -> e.distance),
			ConfiguredEngine.codec("child", Modifier::child)
	).apply(i, ForwardOffsetModifier::new));

	@Override
	public EngineType<ForwardOffsetModifier> type() {
		return EngineRegistry.FORWARD.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().add(ctx.loc().dir().scale(distance.eval(ctx)));
	}

}
