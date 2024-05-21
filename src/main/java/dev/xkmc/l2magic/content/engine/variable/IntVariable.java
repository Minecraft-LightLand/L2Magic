package dev.xkmc.l2magic.content.engine.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;

import java.util.Optional;
import java.util.function.Function;

public record IntVariable(String str, ExpressionHolder exp) implements NumericVariable {

	public static final Codec<IntVariable> CODEC = Codec.STRING.xmap(IntVariable::of, IntVariable::str);

	public static <T> RecordCodecBuilder<T, IntVariable> codec(String str, Function<T, IntVariable> func) {
		return CODEC.fieldOf(str).forGetter(func);
	}

	public static <T> RecordCodecBuilder<T, Optional<IntVariable>> optionalCodec(String str, Function<T, IntVariable> func) {
		return CODEC.optionalFieldOf(str).forGetter(e -> Optional.ofNullable(func.apply(e)));
	}

	public static IntVariable of(String str) {
		return new IntVariable(str, ExpressionHolder.of(str));
	}

	public int eval(EngineContext ctx) {
		return (int) exp.eval(ctx);
	}

}
