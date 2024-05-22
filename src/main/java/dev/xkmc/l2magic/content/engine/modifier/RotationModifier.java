package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record RotationModifier(
		DoubleVariable degree,
		DoubleVariable vertical
) implements Modifier<RotationModifier> {

	public static Codec<RotationModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("degree", e -> e.degree),
			DoubleVariable.optionalCodec("vertical", e -> e.vertical)
	).apply(i, (a, b) -> new RotationModifier(a, b.orElse(DoubleVariable.ZERO))));

	public static RotationModifier of(String str) {
		return new RotationModifier(DoubleVariable.of(str), DoubleVariable.ZERO);
	}

	public static RotationModifier of(String str, String ver) {
		return new RotationModifier(DoubleVariable.of(str), DoubleVariable.of(ver));
	}

	@Override
	public ModifierType<RotationModifier> type() {
		return EngineRegistry.ROTATE.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().rotateDegree(degree.eval(ctx), vertical.eval(ctx));
	}

}
