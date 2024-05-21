package dev.xkmc.l2magic.content.engine.iterator;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;

import javax.annotation.Nullable;
import java.util.Set;

public interface Iterator<T extends Record & Iterator<T>> extends ConfiguredEngine<T> {

	ConfiguredEngine<?> child();

	@Nullable
	String index();

	@Override
	@Nullable
	default Set<String> verificationParameters() {
		String str = index();
		return str == null ? Set.of() : Set.of(str);
	}

}
