package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public record KnockBackProcessor(
		DoubleVariable knockback,
		DoubleVariable angle,
		DoubleVariable tilt
) implements SimpleServerProcessor<KnockBackProcessor> {

	public static final MapCodec<KnockBackProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("knockback", e -> e.knockback),
			DoubleVariable.optionalCodec("angle", e -> e.angle),
			DoubleVariable.optionalCodec("tilt", e -> e.tilt)
	).apply(i, (a, b, c) -> new KnockBackProcessor(a, b.orElse(DoubleVariable.ZERO), c.orElse(DoubleVariable.ZERO))));

	public static KnockBackProcessor of(String str) {
		return new KnockBackProcessor(DoubleVariable.of(str), DoubleVariable.ZERO, DoubleVariable.ZERO);
	}

	public static KnockBackProcessor of(String kv, String angle, String tilt) {
		return new KnockBackProcessor(DoubleVariable.of(kv), DoubleVariable.of(angle), DoubleVariable.of(tilt));
	}

	@Override
	public ProcessorType<KnockBackProcessor> type() {
		return EngineRegistry.KB.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		double kb = (float) knockback.eval(ctx) * 0.5;
		double angle = angle().eval(ctx);
		double tilt = tilt().eval(ctx);
		for (var e : le.living()) {
			var p = e.position().subtract(ctx.loc().pos()).normalize();
			if (angle != 0 || tilt != 0) {
				var ori = Orientation.fromForward(p);
				p = ori.rotateDegrees(angle, tilt);
			}
			p = p.multiply(1, 0, 1).normalize();
			e.knockback(kb, -p.x, -p.z);
		}
	}

}
