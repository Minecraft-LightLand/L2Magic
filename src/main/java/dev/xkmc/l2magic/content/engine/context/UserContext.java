package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public record UserContext(Level level, LivingEntity user, Scheduler scheduler) {

	public boolean canHitEntity(Entity entity) {
		return entity instanceof LivingEntity le && le.isAlive() &&
				!user.isPassengerOfSameVehicle(le) &&
				!le.isAlliedTo(user) && !user.isAlliedTo(le);
	}

}
