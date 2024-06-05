package dev.xkmc.l2magic.content.engine.motion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record SimpleMotion(
		DoubleVariable friction,
		DoubleVariable gravity
) implements Motion<SimpleMotion> {

	public static final Codec<SimpleMotion> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.optionalCodec("friction", e -> e.friction),
			DoubleVariable.optionalCodec("gravity", e -> e.gravity)
	).apply(i, (f, g) -> new SimpleMotion(
			f.orElse(DoubleVariable.ZERO),
			g.orElse(DoubleVariable.ZERO)
	)));

	public static final SimpleMotion ZERO = new SimpleMotion(DoubleVariable.ZERO, DoubleVariable.ZERO);
	public static final SimpleMotion DUST = new SimpleMotion(DoubleVariable.ofVerified("0.02"), DoubleVariable.ZERO);
	public static final SimpleMotion BREAKING = new SimpleMotion(DoubleVariable.ofVerified("0.02"), DoubleVariable.ofVerified("0.04"));

	@Override
	public MotionType<SimpleMotion> type() {
		return EngineRegistry.SIMPLE_MOTION.get();
	}

	@Override
	public ProjectileMovement move(EngineContext ctx, Vec3 vec, Vec3 pos) {
		double f = friction.eval(ctx);
		double g = gravity.eval(ctx);
		return ProjectileMovement.of(vec.scale(1 - f).add(0, -g, 0));
	}

}
