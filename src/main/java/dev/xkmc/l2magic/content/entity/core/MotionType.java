package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.MapCodec;

public interface MotionType<T extends Record & Motion<T>> {

	MapCodec<T> codec();

}
