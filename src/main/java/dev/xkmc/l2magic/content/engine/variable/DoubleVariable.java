package dev.xkmc.l2magic.content.engine.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;

import java.util.Optional;
import java.util.function.Function;

public record DoubleVariable(String str, ExpressionHolder exp) implements NumericVariable {

	public static final Codec<DoubleVariable> CODEC = Codec.STRING.xmap(DoubleVariable::of, DoubleVariable::str);

	public static final DoubleVariable ZERO = of("0");

	public static <T> RecordCodecBuilder<T, DoubleVariable> codec(String str, Function<T, DoubleVariable> func) {
		return CODEC.fieldOf(str).forGetter(func);
	}

	public static <T> RecordCodecBuilder<T, Optional<DoubleVariable>> optionalCodec(String str, Function<T, DoubleVariable> func) {
		return CODEC.optionalFieldOf(str).forGetter(e -> Optional.ofNullable(func.apply(e)));
	}

	public static DoubleVariable of(String str) {
		return new DoubleVariable(str, ExpressionHolder.of(str));
	}

	public double eval(EngineContext ctx) {
		return exp.eval(ctx);
	}

}
