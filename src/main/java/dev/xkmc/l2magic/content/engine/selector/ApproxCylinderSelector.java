package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.helper.CollisionHelper;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.LinkedHashSet;

public record ApproxCylinderSelector(
		DoubleVariable r,
		DoubleVariable y
) implements EntitySelector<ApproxCylinderSelector> {

	public static final MapCodec<ApproxCylinderSelector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("r", ApproxCylinderSelector::r),
			DoubleVariable.codec("y", ApproxCylinderSelector::y)
	).apply(i, ApproxCylinderSelector::new));

	@Override
	public SelectorType<ApproxCylinderSelector> type() {
		return EngineRegistry.CYLINDER.get();
	}

	public SelectedEntities find(Level level, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		double r = r().eval(ctx);
		double y = y().eval(ctx);
		var aabb = new AABB(pos.x - r, pos.y, pos.z - r, pos.x + r, pos.y + y, pos.z + r);
		SelectedEntities list = new SelectedEntities();
		var boxes = CollisionHelper.cylinder(pos, r, y);
		type.collectIntersect(level, ctx, aabb, boxes, list);
		return list;
	}

}
