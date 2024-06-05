package dev.xkmc.l2magic.content.entity.engine;

import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import net.minecraft.world.entity.Entity;

public interface ShootProjectileInstance<T extends Record & ShootProjectileInstance<T>> extends ConfiguredEngine<T> {

	Entity create(EngineContext ctx);

	@Override
	default void execute(EngineContext ctx) {
		if (ctx.user().level().isClientSide()) return;
		ctx.user().level().addFreshEntity(create(ctx));

	}

}
