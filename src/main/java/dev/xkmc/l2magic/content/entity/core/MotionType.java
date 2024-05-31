package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.core.Modifier;

public interface MotionType<T extends Record & Motion<T>> {

	Codec<T> codec();

}
