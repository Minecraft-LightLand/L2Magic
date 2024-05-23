package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.function.BiPredicate;

public enum SelectionType {
	ENEMY((entity, user) -> entity instanceof LivingEntity le && le.isAlive() &&
			!user.isPassengerOfSameVehicle(le) && !le.isAlliedTo(user) && !user.isAlliedTo(le)),
	ALLY((entity, user) -> entity instanceof LivingEntity le && le.isAlive() &&
			(le.isAlliedTo(user) || user.isAlliedTo(le))),
	ALLY_AND_PLAYER((entity, user) -> entity instanceof Player pl && pl.isAlive() ||
			entity instanceof LivingEntity le && le.isAlive() &&
					(le.isAlliedTo(user) || user.isAlliedTo(le))),
	ALL((entity, user) -> entity instanceof LivingEntity le && le.isAlive());

	public static final Codec<SelectionType> CODEC = EngineHelper.enumCodec(SelectionType.class, values());

	private final BiPredicate<Entity, LivingEntity> check;

	SelectionType(BiPredicate<Entity, LivingEntity> check) {
		this.check = check;
	}

	public Iterable<Entity> select(ServerLevel sl, EngineContext ctx, AABB aabb) {
		return EntityStorageCache.get(sl).foreach(aabb, x -> check.test(x, ctx.user().user()));
	}
}
