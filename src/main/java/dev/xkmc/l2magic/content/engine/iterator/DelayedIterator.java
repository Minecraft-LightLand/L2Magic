package dev.xkmc.l2magic.content.engine.iterator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import javax.annotation.Nullable;
import java.util.Optional;

public record DelayedIterator(IntVariable step, IntVariable delay, ConfiguredEngine<?> child, @Nullable String index)
		implements Iterator<DelayedIterator> {

	public static MapCodec<DelayedIterator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("step", DelayedIterator::step),
			IntVariable.codec("delay", DelayedIterator::delay),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index))
	).apply(i, (d, e, f, g) -> new DelayedIterator(d, e, f, g.orElse(null))));

	public DelayedIterator(IntVariable step, IntVariable delay, ConfiguredEngine<?> child) {
		this(step, delay, child, null);
	}

	@Override
	public EngineType<DelayedIterator> type() {
		return EngineRegistry.ITERATE_DELAY.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		int step = step().eval(ctx);
		int delay = delay().eval(ctx);
		if (step > 0) {
			recursion(ctx, 0, step, delay);
		}
	}

	private void recursion(EngineContext ctx, int i, int step, int delay) {
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
