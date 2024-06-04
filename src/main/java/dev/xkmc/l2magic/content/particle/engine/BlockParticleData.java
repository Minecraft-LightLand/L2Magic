package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.particle.render.BlockSprite;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BlockParticleData(
		RenderTypePreset renderType, Block block, @Nullable SpriteGeom geom
) implements ParticleRenderData<BlockParticleData> {

	public static final Codec<BlockParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			ForgeRegistries.BLOCKS.getCodec().fieldOf("block").forGetter(e -> e.block),
			SpriteGeom.CODEC.optionalFieldOf("geom").forGetter(e -> Optional.ofNullable(e.geom))
	).apply(i, (a, b, c) -> new BlockParticleData(a, b, c.orElse(null))));

	@Override
	public ParticleRenderType<BlockParticleData> type() {
		return EngineRegistry.BLOCK_RENDER.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new BlockSprite(
				renderType,
				block.defaultBlockState(),
				BlockPos.containing(ctx.loc().pos()),
				geom == null ? SpriteGeom.breaking(ctx.rand()) : geom
		);
	}

}
