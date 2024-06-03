package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.Codec;

public interface MotionType<T extends Record & Motion<T>> {

	Codec<T> codec();

}
