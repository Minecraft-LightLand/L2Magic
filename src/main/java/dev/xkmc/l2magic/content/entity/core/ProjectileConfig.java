package dev.xkmc.l2magic.content.entity.core;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record ProjectileConfig(
		Set<String> params,
		SelectionType filter,
		@Nullable Motion<?> motion,
		@Nullable ConfiguredEngine<?> tick,
		List<EntityProcessor<?>> hit,
		@Nullable ProjectileRenderData<?> renderer,
		@Nullable BoundingData size,
		@Nullable ConfiguredEngine<?> land,
		@Nullable ConfiguredEngine<?> expire
) {

	public static final Codec<ProjectileConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Codec.STRING).optionalFieldOf("params").forGetter(e -> Optional.of(new ArrayList<>(e.params))),
			SelectionType.CODEC.optionalFieldOf("filter").forGetter(e -> Optional.of(e.filter)),
			Motion.CODEC.optionalFieldOf("motion").forGetter(e -> Optional.ofNullable(e.motion)),
			ConfiguredEngine.optionalCodec("tick", e -> e.tick),
			EntityProcessor.CODEC.listOf().fieldOf("hit").forGetter(e -> e.hit),
			ProjectileRenderData.CODEC.optionalFieldOf("renderer").forGetter(e -> Optional.ofNullable(e.renderer)),
			BoundingData.CODEC.optionalFieldOf("size").forGetter(e -> Optional.ofNullable(e.size)),
			ConfiguredEngine.optionalCodec("land", e -> e.land),
			ConfiguredEngine.optionalCodec("expire", e -> e.expire)
	).apply(i, (params, filter, motion, tick, hit, render, size, land, expire) -> new ProjectileConfig(
			params.map(LinkedHashSet::new).orElse(new LinkedHashSet<>()),
			filter.orElse(SelectionType.NONE),
			motion.orElse(null),
			tick.orElse(null),
			hit,
			render.orElse(null),
			size.orElse(null),
			land.orElse(null),
			expire.orElse(null)
	)));

	public static final Codec<Holder<ProjectileConfig>> HOLDER =
			RegistryFileCodec.create(EngineRegistry.PROJECTILE, CODEC, false);

	@Deprecated
	public ProjectileConfig {

	}

	public void verify(ResourceLocation id) {
		var allParams = Sets.union(ProjectileData.DEFAULT_PARAMS, params);
		var withSche = BuilderContext.withScheduler(L2Magic.LOGGER, id.toString(), allParams);
		var noSche = BuilderContext.instant(L2Magic.LOGGER, id.toString(), allParams);
		if (motion != null) motion.verify(noSche.of("motion"));
		if (tick != null) tick.verify(withSche.of("tick"));
		if (land != null) land.verify(withSche.of("land"));
		if (expire != null) expire.verify(withSche.of("expire"));
		for (int i = 0; i < hit.size(); i++)
			hit.get(i).verify(withSche.of("hit_" + i));
		if (size != null) size.verify(noSche.of("size"));
		if (renderer != null) renderer.verify(noSche.of("renderer"));
	}


	public void verifyOnBuild(BootstrapContext<ProjectileConfig> ctx, DataGenCachedHolder<ProjectileConfig> id) {
		verify(id.key.location());
		id.gen(ctx, this);
	}

	@DataGenOnly
	public static Builder builder(SelectionType filter, String... params) {
		return new Builder(filter, params);
	}

	public static class Builder {

		private final Set<String> params;
		private final SelectionType filter;

		private final List<EntityProcessor<?>> hit = new ArrayList<>();

		private @Nullable Motion<?> motion;
		private @Nullable ConfiguredEngine<?> tick, land, expire;
		private @Nullable ProjectileRenderData<?> renderer;
		private @Nullable BoundingData size;

		private Builder(SelectionType filter, String... params) {
			this.filter = filter;
			this.params = Set.of(params);
		}

		public Builder hit(EntityProcessor<?> processor) {
			hit.add(processor);
			return this;
		}

		public Builder motion(Motion<?> motion) {
			this.motion = motion;
			return this;
		}

		public Builder tick(ConfiguredEngine<?> tick) {
			this.tick = tick;
			return this;
		}

		public Builder land(ConfiguredEngine<?> land) {
			this.land = land;
			return this;
		}

		public Builder expire(ConfiguredEngine<?> expire) {
			this.expire = expire;
			return this;
		}

		public Builder renderer(ProjectileRenderData<?> renderer) {
			this.renderer = renderer;
			return this;
		}

		public Builder size(DoubleVariable size) {
			this.size = new BoundingData(size, false);
			return this;
		}

		public Builder size(BoundingData size) {
			this.size = size;
			return this;
		}

		public ProjectileConfig build() {
			return new ProjectileConfig(params, filter, motion, tick, hit, renderer, size, land, expire);
		}

	}

}
