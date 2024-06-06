package dev.xkmc.l2magic.content.particle.core;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import net.minecraft.util.Mth;

public class LMOrientation {

	private double xo, yo, zo, x, y, z;
	private double rxo, ryo, rzo, rx, ry, rz;

	public void preTick(double nx, double ny, double nz) {
		xo = x = nx;
		yo = y = ny;
		zo = z = nz;
		rxo = rx;
		ryo = ry;
		rzo = rz;
	}

	public void postTick(ProjectileMovement mov, double nx, double ny, double nz) {
		x = nx;
		y = ny;
		z = nz;
		var rot = mov.rot();
		rx = rot.x;
		ry = rot.y;
		rz = rot.z;
	}

	public double getX(double pTick) {
		return Mth.lerp(pTick, xo, x);
	}

	public double getY(double pTick) {
		return Mth.lerp(pTick, yo, y);
	}

	public double getZ(double pTick) {
		return Mth.lerp(pTick, zo, z);
	}

	public double getRX(double pTick) {
		return Mth.lerp(pTick, rxo, rx);
	}

	public double getRY(double pTick) {
		return Mth.lerp(pTick, ryo, ry);
	}

	public double getRZ(double pTick) {
		return Mth.lerp(pTick, rzo, rz);
	}

	public void setRot(double nrx, double nry, double nrz) {
		rx = nrx;
		ry = nry;
		rz = nrz;
	}
}
