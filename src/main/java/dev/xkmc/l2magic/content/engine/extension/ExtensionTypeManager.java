package dev.xkmc.l2magic.content.engine.extension;

import com.google.common.collect.Sets;
import dev.xkmc.l2magic.content.engine.core.Verifiable;

import java.util.IdentityHashMap;
import java.util.Set;

public class ExtensionTypeManager<E extends ExtensionEntry<E, K>, K extends ExtensionKey<E, K>> {

	private static final Set<ExtensionTypeManager<?, ?>> ALL_MANAGERS = Sets.newConcurrentHashSet();

	public static void reload() {
		for (var e : ALL_MANAGERS) e.reset();
	}

	private final IdentityHashMap<Verifiable, E> cache = new IdentityHashMap<>();

	public ExtensionTypeManager() {
		ALL_MANAGERS.add(this);
	}

	public void reset() {
		cache.clear();
	}

	public E get(K spell) {
		var action = spell.getEntry();
		if (cache.containsKey(action)) {
			var e = cache.get(action);
			if (!e.match(spell)) {
				reset();
			} else {
				return e;
			}
		}
		var e = spell.create();
		e.analyze();
		cache.put(action, e);
		return e;
	}
}
