package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record DelayLogic(IntVariable tick, ConfiguredEngine<?> child)
		implements ConfiguredEngine<DelayLogic> {

	public static Codec<DelayLogic> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("tick", DelayLogic::tick),
			ConfiguredEngine.codec("child", DelayLogic::child)
	).apply(i, DelayLogic::new));

	@Override
	public EngineType<DelayLogic> type() {
		return EngineRegistry.DELAY.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		ctx.schedule(tick.eval(ctx), () -> ctx.execute(child));
	}

}
