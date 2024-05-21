package dev.xkmc.l2magic.content.engine.core;

import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2serial.util.Wrappers;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Verifiable {

	@Nullable
	default Set<String> verificationParameters() {
		return null;
	}

	default boolean verify(BuilderContext ctx) {
		EngineHelper.verifyFields(this, ctx, Wrappers.cast(this.getClass()));
		return true;
	}

}
