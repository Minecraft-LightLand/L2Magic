package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public interface ProjectileRenderData<T extends Record & ProjectileRenderData<T>> extends Verifiable {

	Codec<ProjectileRenderData<?>> CODEC = EngineRegistry.PROJECTILE_RENDERER.codec()
			.dispatch(ProjectileRenderData::type, ProjectileRenderType::codec);

	ProjectileRenderType<T> type();

	ProjectileRenderer resolve(EngineContext ctx);

	default void buildRenderer() {

	}

}
