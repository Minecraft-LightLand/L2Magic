package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record RotationModifier(DoubleVariable degree) implements Modifier<RotationModifier> {

	public static Codec<RotationModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("degree", e -> e.degree)
	).apply(i, RotationModifier::new));

	public static RotationModifier of(String str) {
		return new RotationModifier(DoubleVariable.of("rand(0,360)"));
	}

	@Override
	public ModifierType<RotationModifier> type() {
		return EngineRegistry.ROTATE.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().rotateDegree(degree.eval(ctx));
	}

}
