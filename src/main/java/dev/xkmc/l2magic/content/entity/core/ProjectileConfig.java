package dev.xkmc.l2magic.content.entity.core;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
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
		@Nullable EntityProcessor<?> hit,
		@Nullable ProjectileRenderData<?> renderer
) {

	public static final Codec<ProjectileConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Codec.STRING).optionalFieldOf("params").forGetter(e -> Optional.of(new ArrayList<>(e.params))),
			SelectionType.CODEC.optionalFieldOf("filter").forGetter(e -> Optional.of(e.filter)),
			Motion.CODEC.optionalFieldOf("motion").forGetter(e -> Optional.ofNullable(e.motion)),
			ConfiguredEngine.optionalCodec("tick", e -> e.tick),
			EntityProcessor.CODEC.optionalFieldOf("hit").forGetter(e -> Optional.ofNullable(e.hit)),
			ProjectileRenderData.CODEC.optionalFieldOf("renderer").forGetter(e -> Optional.ofNullable(e.renderer))
	).apply(i, (params, filter, motion, tick, hit, render) -> new ProjectileConfig(
			params.map(LinkedHashSet::new).orElse(new LinkedHashSet<>()),
			filter.orElse(SelectionType.NONE),
			motion.orElse(null),
			tick.orElse(null),
			hit.orElse(null),
			render.orElse(null)
	)));

	public static final Codec<Holder<ProjectileConfig>> HOLDER =
			RegistryFileCodec.create(EngineRegistry.PROJECTILE, CODEC, false);

	public void verify(ResourceLocation id) {
		var allParams = Sets.union(ProjectileData.DEFAULT_PARAMS, params);
		var withSche = BuilderContext.withScheduler(L2Magic.LOGGER, id.toString(), allParams);
		var noSche = BuilderContext.instant(L2Magic.LOGGER, id.toString(), allParams);
		if (motion != null) motion.verify(noSche.of("motion"));
		if (tick != null) tick.verify(withSche.of("tick"));
		if (hit != null) hit.verify(noSche.of("hit"));
	}

}
