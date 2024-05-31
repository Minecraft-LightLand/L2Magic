package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public record ProjectileConfig(
		Set<String> params,
		SelectionType filter,
		@Nullable Motion<?> motion,
		@Nullable ConfiguredEngine<?> tick,
		@Nullable EntityProcessor<?> hit
) {

	public static final Codec<ProjectileConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Codec.STRING).optionalFieldOf("params").forGetter(e -> Optional.of(new ArrayList<>(e.params))),
			SelectionType.CODEC.optionalFieldOf("filter").forGetter(e -> Optional.of(e.filter)),
			Motion.CODEC.optionalFieldOf("motion").forGetter(e -> Optional.ofNullable(e.motion)),
			ConfiguredEngine.optionalCodec("tick", e -> e.tick),
			EntityProcessor.CODEC.optionalFieldOf("hit").forGetter(e -> Optional.ofNullable(e.hit))
	).apply(i, (params, filter, motion, tick, hit) -> new ProjectileConfig(
			params.map(LinkedHashSet::new).orElse(new LinkedHashSet<>()),
			filter.orElse(SelectionType.NONE),
			motion.orElse(null),
			tick.orElse(null),
			hit.orElse(null)
	)));

	public void verify(ResourceLocation id) {
		var ctx =  BuilderContext.withScheduler(L2Magic.LOGGER, id.toString(), params);
		if (motion != null) motion.verify(ctx.of("motion"));
		if (tick != null) tick.verify(ctx.of("tick"));
		if (hit != null) hit.verify(ctx.of("hit"));
	}

}
