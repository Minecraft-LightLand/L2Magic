package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;

public record SpellContext(LivingEntity user, Vec3 origin, Vec3 facing, long seed, double tickUsing, double power) {

	public static Set<String> DEFAULT_PARAMS = Set.of("TickUsing", "Power", "CastX", "CastY", "CastZ", "Time");

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
				return getCenter(le).subtract(mob.getEyePosition());
			}
		}
		return le.getForward();
	}

	public Map<String, Double> defaultArgs() {
		return Map.of("TickUsing", tickUsing(),
				"Power", power(),
				"CastX", origin().x,
				"CastY", origin().y,
				"CastZ", origin().z);
	}
}
