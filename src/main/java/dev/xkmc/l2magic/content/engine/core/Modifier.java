package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;

public interface Modifier<T extends Record & Modifier<T>> extends Verifiable {

	Codec<Modifier<?>> CODEC = EngineRegistry.MODIFIER.codec()
			.dispatch(Modifier::type, ModifierType::codec);

	ModifierType<T> type();

	LocationContext modify(EngineContext ctx);

}
