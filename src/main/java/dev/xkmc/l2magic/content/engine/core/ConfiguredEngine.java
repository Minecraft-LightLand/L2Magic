package dev.xkmc.l2magic.content.engine.core;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.util.Wrappers;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Optional;
import java.util.function.Function;

public interface ConfiguredEngine<T extends Record & ConfiguredEngine<T>> {

	Codec<ConfiguredEngine<?>> CODEC = EngineRegistry.REGISTRY.get().byNameCodec()
			.dispatch(ConfiguredEngine::type, EngineType::codec);

	static <T> RecordCodecBuilder<T, ConfiguredEngine<?>> codec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.fieldOf(str).forGetter(func);
	}

	static <T> RecordCodecBuilder<T, Optional<ConfiguredEngine<?>>> optionalCodec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.optionalFieldOf(str).forGetter(e -> Optional.ofNullable(func.apply(e)));
	}

	void execute(EngineContext ctx);

	default T self() {
		return Wrappers.cast(this);
	}

	@MustBeInvokedByOverriders
	default boolean verify(BuilderContext ctx) {
		EngineHelper.verifyVars(self(), ctx, Wrappers.cast(self().getClass()));
		return true;
	}

	EngineType<T> type();

}