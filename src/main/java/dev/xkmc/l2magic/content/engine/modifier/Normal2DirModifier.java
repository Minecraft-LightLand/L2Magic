package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record Normal2DirModifier() implements Modifier<Normal2DirModifier> {

	public static Codec<Normal2DirModifier> CODEC = Codec.unit(new Normal2DirModifier());

	@Override
	public ModifierType<Normal2DirModifier> type() {
		return EngineRegistry.NORMAL_2_DIR.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setDir(ctx.loc().normal());
	}

}
