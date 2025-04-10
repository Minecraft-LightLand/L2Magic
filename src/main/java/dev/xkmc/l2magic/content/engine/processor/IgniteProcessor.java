package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record IgniteProcessor(
		List<EntityProcessor<?>> action,
		IntVariable burnTicks
) implements EntityProcessor<IgniteProcessor> {
	public static final MapCodec<IgniteProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.list(EntityProcessor.CODEC).fieldOf("action").forGetter(e -> e.action),
			IntVariable.codec("burnTicks", e -> e.burnTicks)
	).apply(i, IgniteProcessor::new));

	@Override
	public ProcessorType<IgniteProcessor> type() {
		return EngineRegistry.IGNITE.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		Map<Boolean, List<LivingEntity>> partitioned = le.living().stream()
				.collect(Collectors.partitioningBy(LivingEntity::isOnFire));
		action().forEach(p -> p.process(new SelectedEntities(partitioned.get(true)), ctx));
		var burn = burnTicks.eval(ctx);
		for (var e : le.entries())
			e.root().igniteForTicks(burn);
	}

	@Override
	public boolean serverOnly() {
		for (var e : action)
			if (!e.serverOnly())
				return false;
		return true;
	}

}
