package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public interface SelectorType<T extends Record & EntitySelector<T>> {

	Codec<T> codec();

}
