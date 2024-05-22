package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.helper.CollisionHelper;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashSet;

public record ApproxBallSelector(
		DoubleVariable r
) implements EntitySelector<ApproxBallSelector> {

	public static final Codec<ApproxBallSelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("r", ApproxBallSelector::r)
	).apply(i, ApproxBallSelector::new));

	@Override
	public SelectorType<ApproxBallSelector> type() {
		return EngineRegistry.BALL.get();
	}

	public LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx) {
		Vec3 pos = ctx.loc().pos();
		double r = r().eval(ctx);
		var aabb = AABB.ofSize(pos, r * 2, r * 2, r * 2);
		LinkedHashSet<LivingEntity> list = new LinkedHashSet<>();
		var boxes = CollisionHelper.ball(pos, r);
		for (var e : EntityStorageCache.get(sl).foreach(aabb, ctx.user()::canHitEntity)) {
			if (e instanceof LivingEntity le) {
				var box = le.getBoundingBox();
				if (CollisionHelper.intersects(box, boxes))
					list.add(le);
			}
		}
		return list;
	}

}
