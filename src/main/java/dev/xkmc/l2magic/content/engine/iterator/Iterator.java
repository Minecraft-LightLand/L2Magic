package dev.xkmc.l2magic.content.engine.iterator;

import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;

import javax.annotation.Nullable;
import java.util.Set;

public interface Iterator<T extends Record & Iterator<T>> extends ConfiguredEngine<T> {

	ConfiguredEngine<?> child();

	@Nullable
	String index();

	default Set<String> params() {
		String str = index();
		return str == null ? Set.of() : Set.of(str);
	}

	@Override
	default boolean verify(BuilderContext ctx) {
		boolean verify = ConfiguredEngine.super.verify(ctx);
		return verify & child().verify(index() == null ? ctx.of("child") : ctx.of("child", params()));
	}

}
