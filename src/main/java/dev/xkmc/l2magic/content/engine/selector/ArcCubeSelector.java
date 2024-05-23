package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashSet;

public record ArcCubeSelector(
		IntVariable step,
		DoubleVariable radius,
		DoubleVariable size,
		DoubleVariable minAngle,
		DoubleVariable maxAngle
) implements EntitySelector<ArcCubeSelector> {

	public static final Codec<ArcCubeSelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("step", ArcCubeSelector::step),
			DoubleVariable.codec("radius", ArcCubeSelector::radius),
			DoubleVariable.codec("size", ArcCubeSelector::size),
			DoubleVariable.optionalCodec("minAngle", ArcCubeSelector::minAngle),
			DoubleVariable.optionalCodec("maxAngle", ArcCubeSelector::maxAngle)
	).apply(i, (s, r, x, c, d) -> new ArcCubeSelector(s, r, x,
			c.orElse(DoubleVariable.of("-180")),
			d.orElse(DoubleVariable.of("180"))
	)));

	@Override
	public SelectorType<ArcCubeSelector> type() {
		return EngineRegistry.ARC.get();
	}

	public LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		int step = step().eval(ctx);
		double r = radius().eval(ctx);
		double diam = size.eval(ctx);
		double a0 = minAngle().eval(ctx);
		double a1 = maxAngle().eval(ctx);
		var ori = ctx.loc().getOrientation();
		LinkedHashSet<LivingEntity> list = new LinkedHashSet<>();
		for (int i = 0; i <= step; i++) {
			double a = a0 + (a1 - a0) / step * i;
			Vec3 p = pos.add(ori.rotateDegrees(a).scale(r));
			var aabb = AABB.ofSize(p, diam, diam, diam);
			for (var e : type.select(sl, ctx, aabb)) {
				if (e instanceof LivingEntity le) {
					list.add(le);
				}
			}
		}
		return list;
	}

}
