package dev.xkmc.l2magic.content.entity.motion;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.entity.core.Motion;
import net.minecraft.world.phys.Vec3;

public interface SetPosMotion<T extends Record & SetPosMotion<T>> extends Motion<T> {

	@Override
	default ProjectileMovement move(EngineContext ctx, Vec3 vec, Vec3 pos) {
		ctx = ctx.with(ctx.loc().with(pos));
		var old = ctx.loc();
		ctx = ctx.with(move(ctx));
		var diff = ctx.loc().pos().subtract(old.pos());
		return new ProjectileMovement(diff, ProjectileMovement.of(ctx.loc().dir()).rot());
	}

	LocationContext move(EngineContext ctx);


}
