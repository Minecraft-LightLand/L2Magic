package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.Optional;

public record PushProcessor(
		DoubleVariable speed,
		DoubleVariable angle,
		DoubleVariable tilt,
		PushProcessor.Type vector
) implements SimpleServerProcessor<PushProcessor> {

	public enum Type {
		UNIFORM, TO_CENTER, TO_BOTTOM, HORIZONTAL;

		public PushProcessor of(String speed, String angle, String tilt) {
			return new PushProcessor(DoubleVariable.of(speed), DoubleVariable.of(angle), DoubleVariable.of(tilt), this);
		}

		public PushProcessor of(String speed) {
			return new PushProcessor(DoubleVariable.of(speed), DoubleVariable.ZERO, DoubleVariable.ZERO, this);
		}

	}

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final MapCodec<PushProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("speed", e -> e.speed),
			DoubleVariable.optionalCodec("angle", e -> e.angle),
			DoubleVariable.optionalCodec("tilt", e -> e.tilt),
			TYPE_CODEC.optionalFieldOf("from_center").forGetter(e -> Optional.of(e.vector))
	).apply(i, (a, b, c, d) -> new PushProcessor(a,
			b.orElse(DoubleVariable.ZERO), c.orElse(DoubleVariable.ZERO),
			d.orElse(Type.TO_CENTER))));

	@Deprecated
	public PushProcessor {

	}

	@Override
	public ProcessorType<PushProcessor> type() {
		return EngineRegistry.PUSH_ENTITY.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		double kb = speed.eval(ctx);
		double angle = angle().eval(ctx);
		double tilt = tilt().eval(ctx);
		for (var e : le) {
			var ori = switch (vector) {
				case UNIFORM -> ctx.loc().ori();
				case TO_BOTTOM -> Orientation.fromForward(e.position()
						.subtract(ctx.loc().pos()).normalize());
				case TO_CENTER -> Orientation.fromForward(e.position()
						.add(0, e.getBbHeight() / 2, 0).subtract(ctx.loc().pos()).normalize());
				case HORIZONTAL -> Orientation.fromForward(e.position()
						.subtract(ctx.loc().pos()).multiply(1, 0, 1).normalize());
			};
			var p = ori.forward();
			if (angle != 0 || tilt != 0) {
				p = ori.rotateDegrees(angle, tilt);
			}
			p = p.scale(kb);
			e.push(p.x, p.y, p.z);
			if (e instanceof Player player) {
				player.hurtMarked = true;
			}
		}
	}

}
