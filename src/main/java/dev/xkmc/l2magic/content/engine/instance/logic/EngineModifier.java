package dev.xkmc.l2magic.content.engine.instance.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

import java.util.List;

public record EngineModifier(List<Modifier<?>> modifiers, ConfiguredEngine<?> child)
		implements ConfiguredEngine<EngineModifier> {

	public static final Codec<EngineModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Modifier.CODEC).fieldOf("modifiers").forGetter(e -> e.modifiers),
			ConfiguredEngine.codec("child", e -> e.child)
	).apply(i, EngineModifier::new));

	@Override
	public EngineType<EngineModifier> type() {
		return EngineRegistry.MOVE_ENGINE.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		for (var e : modifiers) {
			ctx = ctx.with(e.modify(ctx));
		}
		child.execute(ctx);
	}

}
