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

public record LoopIterator(IntVariable step, ConfiguredEngine<?> child, @Nullable String index)
		implements Iterator<LoopIterator> {

	public static Codec<LoopIterator> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("step", LoopIterator::step),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index))
	).apply(i, (d, f, g) -> new LoopIterator(d, f, g.orElse(null))));

	@Override
	public EngineType<LoopIterator> type() {
		return EngineRegistry.ITERATE.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		int step = step().eval(ctx);
		for (int i = 0; i < step; i++) {
			child.execute(ctx.iterateOn(ctx.loc(), index, i));
		}
	}

}
