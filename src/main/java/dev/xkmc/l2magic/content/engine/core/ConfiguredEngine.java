package dev.xkmc.l2magic.content.engine.core;


import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2magic.content.engine.iterator.BlockInRangeIterator;
import dev.xkmc.l2magic.content.engine.logic.DelayLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.VariableLogic;
import dev.xkmc.l2magic.content.engine.predicate.AndPredicate;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ConfiguredEngine<T extends Record & ConfiguredEngine<T>>
		extends ParameterizedVerifiable, IExtended<T> {

	Codec<ConfiguredEngine<?>> CODEC = EngineRegistry.ENGINE.codec()
			.dispatch(ConfiguredEngine::type, EngineType::codec);

	static <T> RecordCodecBuilder<T, ConfiguredEngine<?>> codec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.fieldOf(str).forGetter(func);
	}

	static <T> RecordCodecBuilder<T, Optional<ConfiguredEngine<?>>> optionalCodec(String str, Function<T, ConfiguredEngine<?>> func) {
		return CODEC.optionalFieldOf(str).forGetter(e -> Optional.ofNullable(func.apply(e)));
	}

	void execute(EngineContext ctx);

	EngineType<T> type();

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> move(Modifier<?>... mod) {
		return new MoveEngine(List.of(mod), this);
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> delay(IntVariable delay) {
		return new DelayLogic(delay, this);
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> withVariables(String key, String val) {
		return new VariableLogic(key, val, this);
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> withVariables(
			String k1, String v1,
			String k2, String v2
	) {
		return new VariableLogic(k1, v1, new VariableLogic(k2, v2, this));
	}

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> withVariables(
			String k1, String v1,
			String k2, String v2,
			String k3, String v3
	) {
		return new VariableLogic(k1, v1, new VariableLogic(k2, v2, new VariableLogic(k3, v3, this)));
	}


	@DataGenOnly
	@SuppressWarnings("deprecation")
	default ConfiguredEngine<?> withVariables(List<Pair<String, String>> entries) {
		ConfiguredEngine<?> ans = this;
		for (var e : entries.reversed()) {
			ans = new VariableLogic(e.getFirst(), e.getSecond(), ans);
		}
		return ans;
	}

	default ConfiguredEngine<?> circular(
			String radius,
			String delayPerBlock,
			boolean plane,
			@Nullable String variable,
			IPredicate... predicates
	) {
		return new BlockInRangeIterator(DoubleVariable.of(radius),
				plane ? DoubleVariable.ZERO : DoubleVariable.of(radius), DoubleVariable.of(delayPerBlock),
				new PredicateLogic(new AndPredicate(List.of(predicates)),
						this, null),
				variable);
	}

	default ConfiguredEngine<?> circular(
			String radius,
			String height,
			String delayPerBlock,
			@Nullable String variable,
			IPredicate... predicates
	) {
		return new BlockInRangeIterator(DoubleVariable.of(radius),
				DoubleVariable.of(height), DoubleVariable.of(delayPerBlock),
				new PredicateLogic(new AndPredicate(List.of(predicates)),
						this, null),
				variable);
	}

}