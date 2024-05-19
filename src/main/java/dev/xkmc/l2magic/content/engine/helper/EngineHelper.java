package dev.xkmc.l2magic.content.engine.helper;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.variable.Variable;
import dev.xkmc.l2serial.serialization.type_cache.RecordCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EngineHelper {

	private static final Map<Class<?>, EngineHelper> CACHE = new LinkedHashMap<>();

	public static <T extends Record & ConfiguredEngine<T>> EngineHelper get(Class<T> cls) {
		if (CACHE.containsKey(cls)) {
			return CACHE.get(cls);
		}
		var ans = of(cls);
		CACHE.put(cls, ans);
		return ans;
	}

	public static <T extends Record & ConfiguredEngine<T>> void verifyVars(T obj, BuilderContext ctx, Class<T> cls) {
		try {
			for (var e : get(cls).variables) {
				((Variable) e.get(obj)).verify(ctx.of(e.getName()));
			}
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed configuration", e);
		}
	}

	private static EngineHelper of(Class<?> cls) {
		try {
			return new EngineHelper(cls);
		} catch (Exception e) {
			throw new IllegalStateException("class " + cls.getSimpleName() + " failed configuration", e);
		}
	}

	private final List<Field> variables = new ArrayList<>();

	public EngineHelper(Class<?> cls) throws Exception {
		assert cls.isRecord();
		var cache = RecordCache.get(cls);
		for (var e : cache.getFields()) {
			if (Variable.class.isAssignableFrom(e.getType())) {
				variables.add(e);
			}
		}
	}

	public static <T extends Enum<T>> Codec<T> enumCodec(Class<T> cls, T[] vals) {
		return Codec.STRING.xmap(e -> {
			try {
				return Enum.valueOf(cls, e);
			} catch (Exception ex) {
				throw new IllegalArgumentException(e + " is not a valid " + cls.getSimpleName() + ". Valid values are: " + List.of(vals));
			}
		}, Enum::name);
	}
}
