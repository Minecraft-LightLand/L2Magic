package dev.xkmc.l2magic.content.engine.context;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;

public record SpellContext(LivingEntity user, Vec3 origin, Vec3 facing, long seed, double tickUsing, double power) {

	public static Set<String> DEFAULT_PARAMS = Set.of("TickUsing", "Power", "CastX", "CastY", "CastZ");

	public Map<String, Double> defaultArgs() {
		return Map.of("TickUsing", tickUsing(),
				"Power", power(),
				"CastX", origin().x,
				"CastY", origin().y,
				"CastZ", origin().z);
	}
}
