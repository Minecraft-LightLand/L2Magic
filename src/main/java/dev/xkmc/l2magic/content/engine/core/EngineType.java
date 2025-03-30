package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.extension.CodecHolder;
import dev.xkmc.l2magic.content.engine.extension.ExtensionTypeKey;

public class EngineType<T extends Record & ConfiguredEngine<T>> extends CodecHolder<T> {

	public EngineType(ExtensionTypeKey key, MapCodec<T> codec) {
		super(key, codec);
	}

	public interface Factory<T extends Record & ConfiguredEngine<T>> {

		MapCodec<T> codec();

	}

}
