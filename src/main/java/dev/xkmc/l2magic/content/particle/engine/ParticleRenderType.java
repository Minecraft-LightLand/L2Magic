package dev.xkmc.l2magic.content.particle.engine;

import com.mojang.serialization.Codec;

public interface ParticleRenderType<T extends Record & ParticleRenderData<T>> {

	Codec<T> codec();

}
