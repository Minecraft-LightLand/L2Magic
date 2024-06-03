package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ParticleRenderData<T extends Record & ParticleRenderData<T>> extends Verifiable {

	Codec<ParticleRenderData<?>> CODEC = EngineRegistry.PARTICLE.codec()
			.dispatch(ParticleRenderData::type, ParticleRenderType::codec);

	ParticleRenderType<T> type();

	@OnlyIn(Dist.CLIENT)
	ParticleRenderer resolve();

}
