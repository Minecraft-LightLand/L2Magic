package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;

public record PushProcessor(
		DoubleVariable speed,
		DoubleVariable angle,
		DoubleVariable tilt,
		PushProcessor.Type vector
) implements EntityProcessor<PushProcessor> {

	public enum Type {
		UNIFORM, TO_CENTER, TO_BOTTOM
	}

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final Codec<PushProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.codec("speed", e -> e.speed),
			DoubleVariable.optionalCodec("angle", e -> e.angle),
			DoubleVariable.optionalCodec("tilt", e -> e.tilt),
			TYPE_CODEC.optionalFieldOf("from_center").forGetter(e -> Optional.of(e.vector))
	).apply(i, (a, b, c, d) -> new PushProcessor(a,
			b.orElse(DoubleVariable.ZERO), c.orElse(DoubleVariable.ZERO),
			d.orElse(Type.TO_CENTER))));

	@Override
	public ProcessorType<PushProcessor> type() {
		return EngineRegistry.PUSH_ENTITY.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		double kb = speed.eval(ctx);
		double angle = angle().eval(ctx);
		double tilt = tilt().eval(ctx);
		for (var e : le) {
			var p = switch (vector) {
				case UNIFORM -> ctx.loc().dir();
				case TO_CENTER -> e.position().add(0, e.getBbHeight() / 2, 0).subtract(ctx.loc().pos()).normalize();
				case TO_BOTTOM -> e.position().subtract(ctx.loc().pos()).normalize();
			};
			if (angle != 0 || tilt != 0) {
				var ori = Orientation.getOrientation(p, ctx.loc().normal());
				p = ori.rotateDegrees(angle, tilt);
			}
			p = p.scale(kb);
			e.push(p.x, p.y, p.z);
		}
	}

}
