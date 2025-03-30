package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;

public interface SyncedAction<T extends SyncedAction<T> & IExtended<T>> extends IExtended<T> {

	ConfiguredEngine<?> child();

}
