package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import javax.annotation.Nullable;

public record PredicateLogic(
		BooleanVariable predicate,
		@Nullable ConfiguredEngine<?> action,
		@Nullable ConfiguredEngine<?> fallback
) implements ConfiguredEngine<PredicateLogic> {

	public static Codec<PredicateLogic> CODEC = RecordCodecBuilder.create(i -> i.group(
			BooleanVariable.CODEC.fieldOf("predicate").forGetter(e -> e.predicate),
			ConfiguredEngine.optionalCodec("action", PredicateLogic::action),
			ConfiguredEngine.optionalCodec("fallback", PredicateLogic::fallback)
	).apply(i, (a, b, c) -> new PredicateLogic(a, b.orElse(null), c.orElse(null))));

	@Override
	public EngineType<PredicateLogic> type() {
		return EngineRegistry.IF.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (predicate.eval(ctx)) {
			if (action != null) ctx.execute(action);
		} else if (fallback != null) ctx.execute(fallback);
	}

}
