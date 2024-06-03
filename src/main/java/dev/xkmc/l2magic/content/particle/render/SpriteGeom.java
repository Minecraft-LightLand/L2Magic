package dev.xkmc.l2magic.content.particle.render;

import net.minecraft.util.RandomSource;

public record SpriteGeom(double u0, double u1, double v0, double v1) {

	public static final SpriteGeom INSTANCE = new SpriteGeom(0, 16, 0, 16);

	public static SpriteGeom breaking(RandomSource random) {
		double u0 = random.nextDouble() * 12;
		double v0 = random.nextDouble() * 12;
		return new SpriteGeom(u0, u0 + 4, v0, v0 + 4);
	}
}
