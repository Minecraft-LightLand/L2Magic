package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.helper.CollisionHelper;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record ApproxBallSelector(
		DoubleVariable r
) implements EntitySelector<ApproxBallSelector> {

	public static final MapCodec<ApproxBallSelector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("r", ApproxBallSelector::r)
	).apply(i, ApproxBallSelector::new));

	@Override
	public SelectorType<ApproxBallSelector> type() {
		return EngineRegistry.BALL.get();
	}

	public SelectedEntities find(Level level, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		double r = r().eval(ctx);
		var aabb = AABB.ofSize(pos, r * 2, r * 2, r * 2);
		SelectedEntities list = new SelectedEntities();
		var boxes = CollisionHelper.ball(pos, r);
		type.collectIntersect(level, ctx, aabb, boxes, list);
		return list;
	}

}
