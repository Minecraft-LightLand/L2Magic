package dev.xkmc.l2magic.content.engine.motion;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.entity.core.Motion;
import net.minecraft.world.phys.Vec3;

public interface SetDeltaMotion<T extends Record & SetDeltaMotion<T>> extends Motion<T> {

	@Override
	default ProjectileMovement move(EngineContext ctx, Vec3 vec, Vec3 pos) {
		ctx = ctx.with(ctx.loc().with(vec));
		ctx = ctx.with(move(ctx));
		return new ProjectileMovement(ctx.loc().pos(), ProjectileMovement.of(ctx.loc().dir()).rot());
	}

	LocationContext move(EngineContext ctx);


}
