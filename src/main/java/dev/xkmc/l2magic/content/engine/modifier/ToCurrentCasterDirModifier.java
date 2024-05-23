package dev.xkmc.l2magic.content.engine.modifier;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.ModifierType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public record ToCurrentCasterDirModifier() implements Modifier<ToCurrentCasterDirModifier> {

	public static Codec<ToCurrentCasterDirModifier> CODEC = Codec.unit(new ToCurrentCasterDirModifier());

	@Override
	public ModifierType<ToCurrentCasterDirModifier> type() {
		return EngineRegistry.TO_CASTER_DIR.get();
	}

	@Override
	public LocationContext modify(EngineContext ctx) {
		return ctx.loc().setDir(SpellContext.getForward(ctx.user().user()));
	}

}
