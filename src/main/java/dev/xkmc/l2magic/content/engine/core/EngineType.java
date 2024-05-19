package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;

public interface EngineType<T extends Record & ConfiguredEngine<T>> {

	Codec<T> codec();

}
