package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record SetDirectionModifier(DoubleVariable x, DoubleVariable y, DoubleVariable z, ConfiguredEngine<?> child)
		implements Modifier<SetDirectionModifier> {

	public static Codec<SetDirectionModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.optionalCodec("x", SetDirectionModifier::x),
			DoubleVariable.optionalCodec("y", SetDirectionModifier::y),
			DoubleVariable.optionalCodec("z", SetDirectionModifier::z),
			ConfiguredEngine.codec("child", Modifier::child)
	).apply(i, (x, y, z, c) -> new SetDirectionModifier(
			x.orElse(DoubleVariable.ZERO),
			y.orElse(DoubleVariable.ZERO),
			z.orElse(DoubleVariable.ZERO), c)));

	@Override
	public EngineType<SetDirectionModifier> type() {
		return EngineRegistry.DIRECTION.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setDir(new Vec3(
				x.eval(ctx), y.eval(ctx), z.eval(ctx)
		).normalize());
	}

}
