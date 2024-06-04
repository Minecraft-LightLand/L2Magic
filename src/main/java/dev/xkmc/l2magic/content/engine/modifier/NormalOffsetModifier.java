package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record NormalOffsetModifier(DoubleVariable distance) implements Modifier<NormalOffsetModifier> {

	public static Codec<NormalOffsetModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("distance", e -> e.distance)
	).apply(i, NormalOffsetModifier::new));

	public static NormalOffsetModifier of(String str) {
		return new NormalOffsetModifier(DoubleVariable.of(str));
	}

	@Override
	public ModifierType<NormalOffsetModifier> type() {
		return EngineRegistry.NORMAL_OFFSET.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().add(ctx.loc().normal().scale(distance.eval(ctx)));
	}

}
