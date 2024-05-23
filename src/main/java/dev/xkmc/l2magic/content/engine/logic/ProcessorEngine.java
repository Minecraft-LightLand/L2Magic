package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public record ProcessorEngine(
		SelectionType target,
		EntitySelector<?> selector,
		List<EntityProcessor<?>> processors
) implements ConfiguredEngine<ProcessorEngine> {

	public static final Codec<ProcessorEngine> CODEC = RecordCodecBuilder.create(i -> i.group(
			SelectionType.CODEC.fieldOf("target").forGetter(e -> e.target),
			EntitySelector.CODEC.fieldOf("selector").forGetter(e -> e.selector),
			Codec.list(EntityProcessor.CODEC).fieldOf("processors").forGetter(e -> e.processors)
	).apply(i, ProcessorEngine::new));

	@Override
	public EngineType<ProcessorEngine> type() {
		return EngineRegistry.PROCESS_ENGINE.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel sl)) return;
		var set = selector().find(sl, ctx, target);
		for (var p : processors()) {
			p.process(set, ctx);
		}
	}

}
