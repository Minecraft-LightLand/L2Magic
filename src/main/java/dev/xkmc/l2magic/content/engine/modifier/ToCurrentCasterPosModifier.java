package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record ToCurrentCasterPosModifier() implements Modifier<ToCurrentCasterPosModifier> {

	public static Codec<ToCurrentCasterPosModifier> CODEC = Codec.unit(new ToCurrentCasterPosModifier());

	@Override
	public ModifierType<ToCurrentCasterPosModifier> type() {
		return EngineRegistry.TO_CASTER_POS.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().with(ctx.user().user().position());
	}

}
