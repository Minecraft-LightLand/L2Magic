package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public record SetPosModifier(DoubleVariable x, DoubleVariable y, DoubleVariable z)
		implements Modifier<SetPosModifier> {

	public static Codec<SetPosModifier> CODEC = RecordCodecBuilder.create(i -> i.group(
			DoubleVariable.optionalCodec("x", SetPosModifier::x),
			DoubleVariable.optionalCodec("y", SetPosModifier::y),
			DoubleVariable.optionalCodec("z", SetPosModifier::z)
	).apply(i, (x, y, z) -> new SetPosModifier(
			x.orElse(DoubleVariable.ZERO),
			y.orElse(DoubleVariable.ZERO),
			z.orElse(DoubleVariable.ZERO))));

	@Override
	public ModifierType<SetPosModifier> type() {
		return EngineRegistry.POSITION.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().with(new Vec3(
				x.eval(ctx), y.eval(ctx), z.eval(ctx)
		));
	}

}
