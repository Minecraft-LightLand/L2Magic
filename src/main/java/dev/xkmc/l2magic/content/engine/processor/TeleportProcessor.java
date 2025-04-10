package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;

public record TeleportProcessor(
		DoubleVariable x,
		DoubleVariable y,
		DoubleVariable z
) implements SimpleServerProcessor<TeleportProcessor> {

	public static final MapCodec<TeleportProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("x", e -> e.x),
			DoubleVariable.codec("y", e -> e.y),
			DoubleVariable.codec("z", e -> e.z)
	).apply(i, TeleportProcessor::new));

	@Override
	public ProcessorType<TeleportProcessor> type() {
		return EngineRegistry.TP.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		double posX = x().eval(ctx);
		double posY = y().eval(ctx);
		double posZ = z().eval(ctx);
		for (var e : le.entries()) {
			e.root().teleportTo(posX, posY, posZ);
		}
	}
}
