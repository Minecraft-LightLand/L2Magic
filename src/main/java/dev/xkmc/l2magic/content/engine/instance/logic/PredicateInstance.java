package dev.xkmc.l2magic.content.engine.instance.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import javax.annotation.Nullable;

public record PredicateInstance(
		BooleanVariable predicate,
		@Nullable ConfiguredEngine<?> action,
		@Nullable ConfiguredEngine<?> fallback
) implements ConfiguredEngine<PredicateInstance> {

	public static Codec<PredicateInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			BooleanVariable.CODEC.fieldOf("predicate").forGetter(e -> e.predicate),
			ConfiguredEngine.optionalCodec("action", PredicateInstance::action),
			ConfiguredEngine.optionalCodec("fallback", PredicateInstance::fallback)
	).apply(i, (a, b, c) -> new PredicateInstance(a, b.orElse(null), c.orElse(null))));

	@Override
	public EngineType<PredicateInstance> type() {
		return EngineRegistry.IF.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (predicate.eval(ctx)) {
			if (action != null) action.execute(ctx);
		}
		if (fallback != null) fallback.execute(ctx);
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		return ConfiguredEngine.super.verify(ctx) &
				(action == null || action.verify(ctx.of("action"))) &
				(fallback == null || fallback.verify(ctx.of("fallback")));
	}
}
