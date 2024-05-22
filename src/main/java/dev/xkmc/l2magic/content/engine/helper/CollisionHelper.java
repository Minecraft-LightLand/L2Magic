package dev.xkmc.l2magic.content.engine.helper;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CollisionHelper {

	private static final double RATE = 1.56;

	public static AABB[] cylinder(Vec3 center, double r, double y) {
		center = center.add(0, y / 2, 0);
		double d = r * RATE;
		double h = r - d / 2;
		double w = (r * r * Math.PI - d * d) / 4 / h;
		return new AABB[]{
				AABB.ofSize(center, d, y, d),
				AABB.ofSize(center.add(r - h / 2, 0, 0), h, y, w),
				AABB.ofSize(center.add(-r + h / 2, 0, 0), h, y, w),
				AABB.ofSize(center.add(0, 0, r - h / 2), w, y, h),
				AABB.ofSize(center.add(0, 0, -r + h / 2), w, y, h)
		};
	}

	public static AABB[] ball(Vec3 center, double r) {
		double d = r * RATE;
		double h = r - d / 2;
		double w = (r * r * Math.PI - d * d) / 4 / h;
		return new AABB[]{
				AABB.ofSize(center, d, d, d),
				AABB.ofSize(center.add(r - h / 2, 0, 0), h, w, w),
				AABB.ofSize(center.add(-r + h / 2, 0, 0), h, w, w),
				AABB.ofSize(center.add(0, r - h / 2, 0), w, h, w),
				AABB.ofSize(center.add(0, -r + h / 2, 0), w, h, w),
				AABB.ofSize(center.add(0, 0, r - h / 2), w, w, h),
				AABB.ofSize(center.add(0, 0, -r + h / 2), w, w, h)
		};
	}

	public static boolean intersects(AABB box, AABB[] boxes) {
		for (AABB b : boxes) {
			if (box.intersects(b)) {
				return true;
			}
		}
		return false;
	}
}
