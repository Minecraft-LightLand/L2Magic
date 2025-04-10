package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.particle.render.ItemSprite;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ItemParticleData(
		RenderTypePreset renderType, Item item, @Nullable SpriteGeom geom
) implements ParticleRenderData<ItemParticleData> {

	public static final MapCodec<ItemParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item),
			SpriteGeom.CODEC.optionalFieldOf("geom").forGetter(e -> Optional.ofNullable(e.geom))
	).apply(i, (a, b, c) -> new ItemParticleData(a, b, c.orElse(null))));

	@Override
	public ParticleRenderType<ItemParticleData> type() {
		return EngineRegistry.ITEM_RENDER.get();
	}

	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new ItemSprite(
				renderType,
				item.getDefaultInstance(),
				geom == null ? SpriteGeom.breaking(ctx.rand()) : geom
		);
	}

}
