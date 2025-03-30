package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public record DelayedProcessor(
		IntVariable time,
		EntityProcessor<?> child
) implements EntityProcessor<DelayedProcessor> {

	public static final MapCodec<DelayedProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("time", DelayedProcessor::time),
			EntityProcessor.CODEC.fieldOf("child").forGetter(DelayedProcessor::child)
	).apply(i, DelayedProcessor::new));

	@Deprecated
	public DelayedProcessor {

	}

	@Override
	public ProcessorType<DelayedProcessor> type() {
		return EngineRegistry.DELAY_PROCESSOR.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		ctx.schedule(time.eval(ctx), () -> ctx.process(le, child));
	}

	@Override
	public boolean serverOnly() {
		return false;
	}

}
