package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;

public interface SelectorType<T extends Record & EntitySelector<T>> {

	Codec<T> codec();

}
