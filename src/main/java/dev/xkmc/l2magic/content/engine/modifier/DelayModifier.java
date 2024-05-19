package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record DelayModifier(IntVariable tick, ConfiguredEngine<?> child)
		implements ConfiguredEngine<DelayModifier> {

	public static Codec<DelayModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("tick", DelayModifier::tick),
			ConfiguredEngine.codec("child", DelayModifier::child)
	).apply(i, DelayModifier::new));

	@Override
	public EngineType<DelayModifier> type() {
		return EngineRegistry.DELAY.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		ctx.schedule(tick.eval(ctx), () -> child.execute(ctx));
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		return ConfiguredEngine.super.verify(ctx) & child().verify(ctx.of("child"));
	}
}
