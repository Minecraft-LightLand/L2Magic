package dev.xkmc.l2magic.content.entity.renderer;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ProjectileRenderData<T extends Record & ProjectileRenderData<T>> {

	Codec<ProjectileRenderData<?>> CODEC = EngineRegistry.PROJECTILE_RENDERER.codec()
			.dispatch(ProjectileRenderData::type, ProjectileRenderType::codec);

	ProjectileRenderType<T> type();

	@OnlyIn(Dist.CLIENT)
	ProjectileRenderer resolve(EngineContext ctx);

}
