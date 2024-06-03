package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Orientation;
import net.minecraft.world.phys.Vec3;

public record LocationContext(Vec3 pos, Vec3 dir, Vec3 normal) {

	public static final Vec3 UP = new Vec3(0, 1, 0);

	public static LocationContext of(Vec3 pos, Vec3 dir) {
		return of(pos, Orientation.fromForward(dir));
	}

	public static LocationContext of(Vec3 pos, Orientation ori) {
		return of(pos, ori.forward(), ori.normal());
	}

	public static LocationContext of(Vec3 pos, Vec3 dir, Vec3 normal) {
		return new LocationContext(pos, dir, normal);
	}

	public LocationContext with(Vec3 pos) {
		return of(pos, dir, normal);
	}

	public LocationContext add(Vec3 offset) {
		return with(pos.add(offset));
	}

	public LocationContext rotateDegree(double degree) {
		return of(pos, ori().rotDegY(degree));
	}

	public LocationContext rotateDegree(double degree, double vertical) {
		return of(pos, ori().rotDegY(degree).rotDegX(vertical));
	}

	public LocationContext setDir(Vec3 vec3) {
		var side = normal.cross(vec3).normalize();
		if (side.length() < 0.9)
			return of(pos, Orientation.fromForward(vec3));
		var nnor = vec3.cross(normal.cross(vec3));
		return of(pos, vec3, nnor);
	}

	public LocationContext setNormal(Vec3 vec3) {
		var side = dir.cross(vec3).normalize();
		if (side.length() < 0.9)
			return of(pos, Orientation.fromNormal(vec3));
		var ndir = vec3.cross(dir.cross(vec3));
		return of(pos, ndir, vec3);
	}

	public Orientation ori() {
		if (dir.distanceTo(UP) < 0.01 && normal.distanceTo(UP) < 0.01) {
			return Orientation.of(new Vec3(1, 0, 0), UP);
		}
		if (dir.distanceTo(normal) < 0.01) {
			return Orientation.fromNormal(normal);
		}
		if (dir.dot(normal) < 1e-9) {
			return Orientation.of(dir, normal);
		}
		var nnor = dir.cross(normal.cross(dir));
		return Orientation.of(dir, nnor);
	}

}
