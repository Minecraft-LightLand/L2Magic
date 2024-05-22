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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public record RingIterator(DoubleVariable radius,
						   DoubleVariable minAngle, DoubleVariable maxAngle,
						   IntVariable count, boolean maxInclusive,
						   ConfiguredEngine<?> child, @Nullable String index)
		implements Iterator<RingIterator> {

	public static Codec<RingIterator> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("radius", RingIterator::radius),
			DoubleVariable.optionalCodec("minAngle", RingIterator::minAngle),
			DoubleVariable.optionalCodec("maxAngle", RingIterator::maxAngle),
			IntVariable.codec("step", RingIterator::count),
			Codec.BOOL.optionalFieldOf("maxInclusive").forGetter(e -> Optional.of(e.maxInclusive)),
			ConfiguredEngine.codec("child", Iterator::child),
			Codec.STRING.optionalFieldOf("index").forGetter(e -> Optional.ofNullable(e.index()))
	).apply(i, (r, c, d, e, m, f, g) -> new RingIterator(r,
			c.orElse(DoubleVariable.of("-180")), d.orElse(DoubleVariable.of("180")),
			e, m.orElse(false), f, g.orElse(null))));

	@Override
	public EngineType<RingIterator> type() {
		return EngineRegistry.ITERATE_ARC.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		double minAngle = minAngle().eval(ctx);
		double maxAngle = maxAngle().eval(ctx);
		double r = radius.eval(ctx);
		int count = count().eval(ctx);
		var ori = ctx.loc().getOrientation();
		int n = maxInclusive ? count + 1 : count;
		for (int i = 0; i < n; i++) {
			double th = (maxAngle - minAngle) / n * i + minAngle;
			Vec3 dir = ori.rotateDegrees(th);
			Vec3 off = dir.scale(r);
			ctx.iterateOn(LocationContext.of(ctx.loc().pos().add(off), dir, ori.normal()), index, i, child);
		}
	}

}
