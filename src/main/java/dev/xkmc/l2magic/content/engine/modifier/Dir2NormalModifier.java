package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record Dir2NormalModifier() implements Modifier<Dir2NormalModifier> {

	public static Codec<Dir2NormalModifier> CODEC = Codec.unit(new Dir2NormalModifier());

	@Override
	public ModifierType<Dir2NormalModifier> type() {
		return EngineRegistry.DIR_2_NORMAL.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setNormal(ctx.loc().dir());
	}

}
