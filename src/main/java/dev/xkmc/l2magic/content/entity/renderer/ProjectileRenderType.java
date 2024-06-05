package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.serialization.Codec;

public interface ProjectileRenderType<T extends Record & ProjectileRenderData<T>> {

	Codec<T> codec();

}
