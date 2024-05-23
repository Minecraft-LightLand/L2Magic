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

public record LinearCubeSelector(
		IntVariable step,
		DoubleVariable size
) implements EntitySelector<LinearCubeSelector> {

	public static final Codec<LinearCubeSelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntVariable.codec("step", LinearCubeSelector::step),
			DoubleVariable.codec("size", LinearCubeSelector::size)
	).apply(i, LinearCubeSelector::new));

	@Override
	public SelectorType<LinearCubeSelector> type() {
		return EngineRegistry.LINEAR.get();
	}

	public LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		int step = step().eval(ctx);
		double diam = size().eval(ctx);
		LinkedHashSet<LivingEntity> list = new LinkedHashSet<>();
		for (int i = 0; i <= step; i++) {
			Vec3 p = pos.add(ctx.loc().dir().scale(i * diam));
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
