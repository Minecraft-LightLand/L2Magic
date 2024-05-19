package dev.xkmc.l2magic.content.engine.modifier;

import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface Modifier<T extends Record & Modifier<T>> extends ConfiguredEngine<T> {

	ConfiguredEngine<?> child();

	LocationContext modify(EngineContext ctx);

	@Override
	default void execute(EngineContext ctx) {
		child().execute(ctx.with(modify(ctx)));
	}

	@MustBeInvokedByOverriders
	@Override
	default boolean verify(BuilderContext ctx) {
		return ConfiguredEngine.super.verify(ctx) & child().verify(ctx.of("child"));
	}

}
