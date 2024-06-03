package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public record SpellContext(LivingEntity user, Vec3 origin, Orientation facing, long seed, double tickUsing,
						   double power) {

	public static Set<String> DEFAULT_PARAMS = Set.of("TickUsing", "Power", "CastX", "CastY", "CastZ");

	public static Vec3 getCenter(LivingEntity le) {
		return le.position().add(0, le.getBbHeight() / 2f, 0);
	}

	public static Vec3 getForward(LivingEntity le) {
		if (le instanceof Player player) {
			return RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 1);
		}
		if (le instanceof Mob mob) {
			var target = mob.getTarget();
			if (target != null) {
				return getCenter(target).subtract(mob.getEyePosition()).normalize();
			}
		}
		return le.getForward();
	}

	@Nullable
	public static LivingEntity getTarget(LivingEntity le) {
		if (le instanceof Player player) {
			return RayTraceUtil.serverGetTarget(player);
		}
		if (le instanceof Mob mob) {
			return mob.getTarget();
		}
		return null;
	}

	@Nullable
	public static SpellContext castSpell(LivingEntity user, SpellAction spell, int useTick, double power, int distance) {
		Level level = user.level();
		Vec3 pos;
		Orientation ori;
		switch (spell.triggerType()) {
			case SELF_POS -> {
				pos = user.position();
				ori = Orientation.regular();
			}
			case TARGET_POS -> {
				var start = user.getEyePosition();
				var forward = SpellContext.getForward(user);
				var end = start.add(forward.scale(distance));
				AABB box = (new AABB(start, end)).inflate(1.0);
				var bhit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user));
				var ehit = ProjectileUtil.getEntityHitResult(level, user, start, end, box, e -> true);
				if ((ehit == null || ehit.getType() == HitResult.Type.MISS) && bhit.getType() == HitResult.Type.MISS) {
					return null;
				}
				pos = ehit != null && ehit.getLocation().distanceToSqr(start) < bhit.getLocation().distanceToSqr(start) ?
						ehit.getLocation() : bhit.getLocation();
				ori = Orientation.regular();
			}
			case HORIZONTAL_FACING -> {
				var dir = SpellContext.getForward(user).multiply(1, 0, 1).normalize();
				pos = user.position();
				if (dir.length() < 0.5) return null;
				ori = Orientation.fromForward(dir);
			}
			case FACING_BACK -> {
				var dir = SpellContext.getForward(user);
				pos = user.getEyePosition().add(dir.scale(-1));
				ori = Orientation.fromForward(dir);
			}
			case FACING_FRONT -> {
				var dir = SpellContext.getForward(user);
				pos = user.getEyePosition().add(dir);
				ori = Orientation.fromForward(dir);
			}
			case TARGET_ENTITY -> {
				var target = getTarget(user);
				if (target != null) {
					pos = target.position();
					ori = Orientation.regular();
				} else return null;
			}
			default -> {
				return null;
			}
		}
		long seed = 0;
		if (!level.isClientSide()) {
			seed = ThreadLocalRandom.current().nextLong();
		}
		return new SpellContext(user, pos, ori, seed, useTick, power);
	}

	public Map<String, Double> defaultArgs() {
		return Map.of("TickUsing", tickUsing(),
				"Power", power(),
				"CastX", origin().x,
				"CastY", origin().y,
				"CastZ", origin().z);
	}
}
