package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.particle.render.FixedParticleSprite;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public record StaticTextureParticleData(
		RenderTypePreset renderType, ParticleType<?> particle,
		int age, int total
) implements ParticleRenderData<StaticTextureParticleData> {

	public static final Codec<StaticTextureParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
			RenderTypePreset.CODEC.fieldOf("renderType").forGetter(e -> e.renderType),
			ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("particle").forGetter(e -> e.particle),
			Codec.INT.fieldOf("age").forGetter(e -> e.age),
			Codec.INT.fieldOf("total").forGetter(e -> e.total)
	).apply(i, StaticTextureParticleData::new));

	@Override
	public ParticleRenderType<StaticTextureParticleData> type() {
		return EngineRegistry.STATIC_RENDER.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		var rl = ForgeRegistries.PARTICLE_TYPES.getKey(particle);
		assert rl != null;
		return new FixedParticleSprite(renderType, rl, age, total);
	}

}
