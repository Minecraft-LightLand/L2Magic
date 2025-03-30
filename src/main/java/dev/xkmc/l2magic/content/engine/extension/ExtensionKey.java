package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2magic.content.engine.core.Verifiable;

public interface ExtensionKey<E extends ExtensionEntry<E, K>, K extends ExtensionKey<E, K>> {

	Verifiable getEntry();

	E create();

}
