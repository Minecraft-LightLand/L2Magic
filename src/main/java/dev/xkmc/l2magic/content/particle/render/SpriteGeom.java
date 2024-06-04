package dev.xkmc.l2magic.content.particle.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

public record SpriteGeom(double u0, double u1, double v0, double v1) {

	public static final Codec<SpriteGeom> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.DOUBLE.fieldOf("x").forGetter(e -> e.u0),
			Codec.DOUBLE.fieldOf("y").forGetter(e -> e.v0),
			Codec.DOUBLE.fieldOf("w").forGetter(e -> e.u1 - e.u0),
			Codec.DOUBLE.fieldOf("h").forGetter(e -> e.v1 - e.v0)
	).apply(i, (x, y, w, h) -> new SpriteGeom(x, x + w, y, y + h)));

	public static final SpriteGeom INSTANCE = new SpriteGeom(0, 16, 0, 16);

	public static SpriteGeom breaking(RandomSource random) {
		double u0 = random.nextDouble() * 12;
		double v0 = random.nextDouble() * 12;
		return new SpriteGeom(u0, u0 + 4, v0, v0 + 4);
	}
}
