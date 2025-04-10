package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record LinearCubeSelector(
		IntVariable step,
		DoubleVariable size
) implements EntitySelector<LinearCubeSelector> {

	public static final MapCodec<LinearCubeSelector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("step", LinearCubeSelector::step),
			DoubleVariable.codec("size", LinearCubeSelector::size)
	).apply(i, LinearCubeSelector::new));

	@Override
	public SelectorType<LinearCubeSelector> type() {
		return EngineRegistry.LINEAR.get();
	}

	public SelectedEntities find(Level level, EngineContext ctx, SelectionType type) {
		Vec3 pos = ctx.loc().pos();
		int step = step().eval(ctx);
		double diam = size().eval(ctx);
		SelectedEntities list = new SelectedEntities();
		for (int i = 0; i <= step; i++) {
			Vec3 p = pos.add(ctx.loc().dir().scale(i * diam));
			var aabb = AABB.ofSize(p, diam, diam, diam);
			type.collect(level, ctx, aabb, list);
		}
		return list;
	}

}
