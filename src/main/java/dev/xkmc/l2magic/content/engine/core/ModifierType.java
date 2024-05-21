package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;

public interface ModifierType<T extends Record & Modifier<T>> {

	Codec<T> codec();

}
