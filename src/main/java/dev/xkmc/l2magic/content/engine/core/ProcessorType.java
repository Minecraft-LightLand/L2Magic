package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;

public interface ProcessorType<T extends Record & EntityProcessor<T>> {

	Codec<T> codec();

}
