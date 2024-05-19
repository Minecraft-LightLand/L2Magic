package dev.xkmc.l2magic.content.engine.iterator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import javax.annotation.Nullable;
import java.util.Optional;

public record DelayedIterator(IntVariable step, IntVariable delay, ConfiguredEngine<?> child, @Nullable String index)
		implements Iterator<DelayedIterator> {

	public static Codec<DelayedIterator> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("step", DelayedIterator::step),
			IntVariable.codec("delay", DelayedIterator::delay),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index))
	).apply(i, (d, e, f, g) -> new DelayedIterator(d, e, f, g.orElse(null))));

	@Override
	public EngineType<DelayedIterator> type() {
		return EngineRegistry.ITERATE_DELAY.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		int step = step().eval(ctx);
		for (int i = 0; i < step; i++) {
			int I = i;
			if (i == 0) child.execute(ctx.iterateOn(ctx.loc(), index, 0));
			else ctx.schedule(i, () -> child.execute(ctx.iterateOn(ctx.loc(), index, I)));
		}
	}

}
