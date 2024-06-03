package dev.xkmc.l2magic.content.particle.render;

import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public interface RandomColorParticle {

	default Vector3f randomizeColor(RandomSource r, Vector3f color) {
		float f = r.nextFloat() * 0.4F + 0.6F;
		return new Vector3f(randomizeValue(r, color.x, f),
				randomizeValue(r, color.y, f),
				randomizeValue(r, color.z, f));
	}

	default float randomizeValue(RandomSource r, float a, float b) {
		return (r.nextFloat() * 0.2F + 0.8F) * a * b;
	}

}
