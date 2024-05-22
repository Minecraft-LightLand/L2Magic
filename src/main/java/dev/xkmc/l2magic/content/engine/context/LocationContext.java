package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Orientation;
import net.minecraft.world.phys.Vec3;

public record LocationContext(Vec3 pos, Vec3 dir, Vec3 normal) {

	public static final Vec3 UP = new Vec3(0, 1, 0);

	public static LocationContext of(Vec3 pos, Vec3 dir) {
		return of(pos, dir, UP);
	}

	public static LocationContext of(Vec3 pos, Vec3 dir, Vec3 normal) {
		return new LocationContext(pos, dir, normal);
	}

	public LocationContext with(Vec3 pos) {
		return new LocationContext(pos, dir, normal);
	}

	public LocationContext add(Vec3 offset) {
		return with(pos.add(offset));
	}

	public LocationContext rotateDegree(double degree) {
		return new LocationContext(pos, getOrientation().rotateDegrees(degree), normal);
	}

	public LocationContext setDir(Vec3 vec3) {
		return new LocationContext(pos, vec3, normal);
	}

	public Orientation getOrientation() {
		if (dir.distanceTo(UP) < 0.01 && normal.distanceTo(UP) < 0.01) {
			return Orientation.getOrientation(new Vec3(1, 0, 0), UP);
		}
		return Orientation.getOrientation(dir, normal);
	}

}
