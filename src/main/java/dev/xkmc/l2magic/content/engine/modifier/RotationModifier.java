package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record RotationModifier(DoubleVariable degree, ConfiguredEngine<?> child)
		implements Modifier<RotationModifier> {

	public static Codec<RotationModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("degree", e -> e.degree),
			ConfiguredEngine.codec("child", Modifier::child)
	).apply(i, RotationModifier::new));

	@Override
	public EngineType<RotationModifier> type() {
		return EngineRegistry.ROTATE.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().rotateDegree(degree.eval(ctx));
	}

}
