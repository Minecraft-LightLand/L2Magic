package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2serial.util.Wrappers;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ExtensionHolder<V extends IExtended<V>> {

	private final Map<Class<?>, Extension<?, V>> map = new LinkedHashMap<>();

	public final ExtensionTypeKey key;

	protected ExtensionHolder(ExtensionTypeKey key) {
		this.key = key;
	}

	public <E> void add(Extension<E, V> ext) {
		map.put(ext.getType(), ext);
	}

	@Nullable
	public <E> Extension<E, V> get(Class<E> cls) {
		return Wrappers.cast(map.get(cls));
	}


}
