package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.content.particle.render.SimpleParticleSprite;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public record SimpleParticleData(
		RenderTypePreset renderType, ParticleType<?> particle
) implements ParticleRenderData<SimpleParticleData> {

	public static final Codec<SimpleParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("particle").forGetter(e -> e.particle)
	).apply(i, SimpleParticleData::new));

	@Override
	public ParticleRenderType<SimpleParticleData> type() {
		return EngineRegistry.SIMPLE_RENDER.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		var rl = ForgeRegistries.PARTICLE_TYPES.getKey(particle);
		assert rl != null;
		return new SimpleParticleSprite(renderType, rl);
	}

}
