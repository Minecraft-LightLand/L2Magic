package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.*;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record RandomOffsetModifier(Type shape, DoubleVariable x, DoubleVariable y, DoubleVariable z)
		implements Modifier<RandomOffsetModifier> {

	public enum Type {
		RECT, BALL, GAUSSIAN
	}

	public static Codec<RandomOffsetModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			EngineHelper.enumCodec(Type.class, Type.values()).fieldOf("shape").forGetter(e -> e.shape),
			DoubleVariable.optionalCodec("x", RandomOffsetModifier::x),
			DoubleVariable.optionalCodec("y", RandomOffsetModifier::y),
			DoubleVariable.optionalCodec("z", RandomOffsetModifier::z)
	).apply(i, (t, x, y, z) -> new RandomOffsetModifier(t,
			x.orElse(DoubleVariable.ZERO),
			y.orElse(DoubleVariable.ZERO),
			z.orElse(DoubleVariable.ZERO))));

	@Override
	public ModifierType<RandomOffsetModifier> type() {
		return EngineRegistry.RANDOM_OFFSET.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		Vec3 r = switch (shape) {
			case RECT -> new Vec3(
					ctx.rand().nextDouble() * 2 - 1,
					ctx.rand().nextDouble() * 2 - 1,
					ctx.rand().nextDouble() * 2 - 1
			);
			case BALL -> new Vec3(
					ctx.rand().nextGaussian(),
					ctx.rand().nextGaussian(),
					ctx.rand().nextGaussian()
			).normalize();
			case GAUSSIAN -> new Vec3(
					ctx.rand().nextGaussian(),
					ctx.rand().nextGaussian(),
					ctx.rand().nextGaussian()
			);
		};
		return ctx.loc().add(r.multiply(x.eval(ctx), y.eval(ctx), z.eval(ctx)));
	}

}
