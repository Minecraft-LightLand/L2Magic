package dev.xkmc.l2magic.content.engine.iterator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import javax.annotation.Nullable;
import java.util.Optional;

public record WhileDelayedIterator(
		IntVariable step, IntVariable delay, IPredicate condition, ConfiguredEngine<?> child, @Nullable String index
) implements Iterator<WhileDelayedIterator> {

	public static MapCodec<WhileDelayedIterator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("step", WhileDelayedIterator::step),
			IntVariable.codec("delay", WhileDelayedIterator::delay),
			IPredicate.CODEC.fieldOf("condition").forGetter(WhileDelayedIterator::condition),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index))
	).apply(i, (d, e, c, f, g) -> new WhileDelayedIterator(d, e, c, f, g.orElse(null))));

	@Override
	public EngineType<WhileDelayedIterator> type() {
		return EngineRegistry.WHILE_DELAY.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		int step = step().eval(ctx);
		int delay = delay().eval(ctx);
		recursion(ctx, 0, step, delay);
	}

	private void recursion(EngineContext ctx, int i, int step, int delay) {
		if (!ctx.test(ctx.loc(), index, i, condition))
			return;
		ctx.iterateOn(ctx.loc(), index, i, child);
		int next = i + 1;
		if (next < step) {
			ctx.schedule(delay, () -> recursion(ctx, next, step, delay));
		}
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		return Iterator.super.verify(ctx) & ctx.requiresScheduler();
	}
}
