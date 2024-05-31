package dev.xkmc.l2magic.content.engine.iterator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

public record RingRandomIterator(DoubleVariable minRadius, DoubleVariable maxRadius,
								 DoubleVariable minAngle, DoubleVariable maxAngle,
								 IntVariable count,
								 ConfiguredEngine<?> child, @Nullable String index)
		implements Iterator<RingRandomIterator> {

	public static Codec<RingRandomIterator> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.optionalCodec("minRadius", RingRandomIterator::minRadius),
			DoubleVariable.codec("maxRadius", RingRandomIterator::maxRadius),
			DoubleVariable.optionalCodec("minAngle", RingRandomIterator::minAngle),
			DoubleVariable.optionalCodec("maxAngle", RingRandomIterator::maxAngle),
			IntVariable.codec("count", RingRandomIterator::count),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index()))
	).apply(i, (a, b, c, d, e, f, g) -> new RingRandomIterator(
			a.orElse(DoubleVariable.ZERO), b,
			c.orElse(DoubleVariable.of("-180")), d.orElse(DoubleVariable.of("180")),
			e, f, g.orElse(null))));

	@Override
	public EngineType<RingRandomIterator> type() {
		return EngineRegistry.RANDOM_FAN.get();
	}

	private static double randomRadius(double min, double max, RandomSource rand) {
		double a = rand.nextDouble();
		double b = rand.nextDouble();
		double mid = (max + min) / 2;
		a = min + (max - min) * a;
		b *= mid;
		if (b > a) {
			a = mid * 2 - a;
		}
		return a;
	}

	@Override
	public void execute(EngineContext ctx) {
		double minRadius = minRadius().eval(ctx);
		double maxRadius = maxRadius().eval(ctx);
		double minAngle = minAngle().eval(ctx);
		double maxAngle = maxAngle().eval(ctx);
		int count = count().eval(ctx);
		var ori = ctx.loc().ori();
		for (int i = 0; i < count; i++) {
			double th = ctx.rand().nextDouble() * (maxAngle - minAngle) + minAngle;
			double r = randomRadius(minRadius, maxRadius, ctx.rand());
			var dir = ori.rotDegY(th);
			Vec3 off = dir.forward().scale(r);
			var param = new LinkedHashMap<>(ctx.parameters());
			if (index != null && !index.isEmpty()) {
				param.put(index, (double) i);
				param.put(index + "_angle", th);
				param.put(index + "_radius", r);
			}
			ctx.execute(LocationContext.of(ctx.loc().pos().add(off), dir), param, child);
		}
	}

	@Override
	public Set<String> verificationParameters() {
		if (index == null) return null;
		return Set.of(index, index + "_angle", index + "_radius");
	}

}
