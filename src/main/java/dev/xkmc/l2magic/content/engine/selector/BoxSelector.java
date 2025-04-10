package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.LinkedHashSet;
import java.util.Optional;

public record BoxSelector(
		DoubleVariable r,
		DoubleVariable y,
		boolean center
) implements EntitySelector<BoxSelector> {

	public static final MapCodec<BoxSelector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("size", BoxSelector::r),
			DoubleVariable.codec("y", BoxSelector::y),
			Codec.BOOL.optionalFieldOf("center").forGetter(e -> Optional.of(e.center))
	).apply(i, (a, b, c) -> new BoxSelector(a, b, c.orElse(false))));

	@Override
	public SelectorType<BoxSelector> type() {
		return EngineRegistry.BOX.get();
	}

	public SelectedEntities find(Level level, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		double r = r().eval(ctx) / 2;
		double y = y().eval(ctx) / 2;
		double y0 = center ? pos.y : pos.y + y;
		var aabb = new AABB(
				pos.x - r, y0 - y, pos.z - r,
				pos.x + r, y0 + y, pos.z + r
		);
		SelectedEntities list = new SelectedEntities();
		type.collect(level, ctx, aabb, list);
		return list;
	}

}
