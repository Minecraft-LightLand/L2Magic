package dev.xkmc.l2magic.content.engine.variable;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface ColorVariable extends Variable {

	Codec<RGB> CODEC_RGB = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("r", RGB::r),
			DoubleVariable.codec("g", RGB::g),
			DoubleVariable.codec("b", RGB::b)
	).apply(i, RGB::new));

	Codec<HSB> CODEC_HSB = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("h", HSB::h),
			DoubleVariable.codec("s", HSB::s),
			DoubleVariable.codec("b", HSB::b)
	).apply(i, HSB::new));

	Codec<Static> CODEC_STATIC = Codec.STRING.xmap(e -> Static.of(Integer.parseInt(e, 16)), e -> Integer.toString(e.val, 16));

	Codec<ColorVariable> CODEC = Codec.either(CODEC_STATIC, Codec.either(CODEC_RGB, CODEC_HSB)
					.xmap(e -> e.map(x -> x, x -> x), e -> e instanceof RGB a ? Either.left(a) : Either.right((HSB) e)))
			.xmap(e -> e.map(x -> x, x -> x), e -> e instanceof Static a ? Either.left(a) : Either.right((Record & ColorVariable) e));

	record RGB(DoubleVariable r, DoubleVariable g, DoubleVariable b) implements ColorVariable {

		@Override
		public Vector3f eval(EngineContext ctx) {
			return new Vector3f((float) r.eval(ctx), (float) g.eval(ctx), (float) b.eval(ctx));
		}
	}

	record HSB(DoubleVariable h, DoubleVariable s, DoubleVariable b) implements ColorVariable {

		public static int HSBtoRGB(float hue, float saturation, float brightness) {
			int r = 0, g = 0, b = 0;
			if (saturation == 0) {
				r = g = b = (int) (brightness * 255.0f + 0.5f);
			} else {
				float h = (hue - (float) Math.floor(hue)) * 6.0f;
				float f = h - (float) Math.floor(h);
				float p = brightness * (1.0f - saturation);
				float q = brightness * (1.0f - saturation * f);
				float t = brightness * (1.0f - (saturation * (1.0f - f)));
				switch ((int) h) {
					case 0:
						r = (int) (brightness * 255.0f + 0.5f);
						g = (int) (t * 255.0f + 0.5f);
						b = (int) (p * 255.0f + 0.5f);
						break;
					case 1:
						r = (int) (q * 255.0f + 0.5f);
						g = (int) (brightness * 255.0f + 0.5f);
						b = (int) (p * 255.0f + 0.5f);
						break;
					case 2:
						r = (int) (p * 255.0f + 0.5f);
						g = (int) (brightness * 255.0f + 0.5f);
						b = (int) (t * 255.0f + 0.5f);
						break;
					case 3:
						r = (int) (p * 255.0f + 0.5f);
						g = (int) (q * 255.0f + 0.5f);
						b = (int) (brightness * 255.0f + 0.5f);
						break;
					case 4:
						r = (int) (t * 255.0f + 0.5f);
						g = (int) (p * 255.0f + 0.5f);
						b = (int) (brightness * 255.0f + 0.5f);
						break;
					case 5:
						r = (int) (brightness * 255.0f + 0.5f);
						g = (int) (p * 255.0f + 0.5f);
						b = (int) (q * 255.0f + 0.5f);
						break;
				}
			}
			return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
		}

		@Override
		public Vector3f eval(EngineContext ctx) {
			return Vec3.fromRGB24(HSBtoRGB((float) h.eval(ctx), (float) s.eval(ctx), (float) b.eval(ctx))).toVector3f();
		}

	}

	record Static(int val, Vector3f color) implements ColorVariable {

		public static Static of(int val) {
			return new Static(val, Vec3.fromRGB24(val).toVector3f());
		}

		@Override
		public Vector3f eval(EngineContext ctx) {
			return color;
		}

	}

	Vector3f eval(EngineContext ctx);

}
