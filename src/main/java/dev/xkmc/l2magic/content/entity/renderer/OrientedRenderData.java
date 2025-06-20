package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.resources.ResourceLocation;

public record OrientedRenderData(
		ResourceLocation texture
) implements ProjectileRenderData<OrientedRenderData> {

	public static final MapCodec<OrientedRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(OrientedRenderData::texture)
	).apply(i, OrientedRenderData::new));

	@Override
	public ProjectileRenderType<OrientedRenderData> type() {
		return EngineRegistry.PR_SIMPLE.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new OrientedTextureRenderer(texture);
	}

	public void buildRenderer() {
		ProjTypeHolder.wrap(new LMProjectileType(texture));
	}

}
