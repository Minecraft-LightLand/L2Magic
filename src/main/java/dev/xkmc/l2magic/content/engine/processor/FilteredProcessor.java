package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record FilteredProcessor(
		EntityFilter<?> filter,
		List<EntityProcessor<?>> action,
		List<EntityProcessor<?>> fallback
) implements EntityProcessor<FilteredProcessor> {

	public static final MapCodec<FilteredProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityFilter.CODEC.fieldOf("filter").forGetter(e -> e.filter),
			Codec.list(EntityProcessor.CODEC).fieldOf("action").forGetter(e -> e.action),
			Codec.list(EntityProcessor.CODEC).fieldOf("fallback").forGetter(e -> e.fallback)
	).apply(i, FilteredProcessor::new));

	@Override
	public ProcessorType<FilteredProcessor> type() {
		return EngineRegistry.FILTERED.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		Map<Boolean, List<LivingEntity>> partitioned = le.living().stream()
				.collect(Collectors.partitioningBy(e -> filter.test(e, ctx)));
		action().forEach(p -> p.process(new SelectedEntities(partitioned.get(true)), ctx));
		fallback().forEach(p -> p.process(new SelectedEntities(partitioned.get(false)), ctx));
	}

	@Override
	public boolean serverOnly() {
		for (var e : action)
			if (!e.serverOnly())
				return false;
		for (var e : fallback)
			if (!e.serverOnly())
				return false;
		return true;
	}


}
