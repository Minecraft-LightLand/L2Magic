package dev.xkmc.l2magic.content.engine.logic;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.EngineContextData;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.extension.SyncedAction;
import dev.xkmc.l2magic.content.engine.extension.SyncedActionEntry;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record SyncedLogic(
		ConfiguredEngine<?> child
) implements ConfiguredEngine<SyncedLogic>, SyncedAction<SyncedLogic> {

	public static final MapCodec<SyncedLogic> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ConfiguredEngine.CODEC.fieldOf("child").forGetter(SyncedLogic::child)
	).apply(i, SyncedLogic::new));

	@Override
	public void execute(EngineContext ctx) {
		SyncedActionEntry.run(ctx, EngineContextData.of(ctx), this);
	}

	@Override
	public EngineType<SyncedLogic> type() {
		return EngineRegistry.SYNC.get();
	}
}
