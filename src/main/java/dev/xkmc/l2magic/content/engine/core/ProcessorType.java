package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.extension.CodecHolder;
import dev.xkmc.l2magic.content.engine.extension.ExtensionTypeKey;

public class ProcessorType<T extends Record & EntityProcessor<T>> extends CodecHolder<T> {

	public ProcessorType(ExtensionTypeKey key, MapCodec<T> codec) {
		super(key, codec);
	}

	public interface Factory<T extends Record & EntityProcessor<T>> {

		MapCodec<T> codec();

	}

}
