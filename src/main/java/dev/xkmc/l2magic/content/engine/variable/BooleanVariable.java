package dev.xkmc.l2magic.content.engine.variable;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;

public record BooleanVariable(String str, ExpressionHolder exp) implements Variable {

	public static final Codec<BooleanVariable> CODEC = Codec.STRING.xmap(BooleanVariable::of, BooleanVariable::str);

	public static BooleanVariable of(String str) {
		return new BooleanVariable(str, ExpressionHolder.of(str));
	}

	public boolean eval(EngineContext ctx) {
		return exp.eval(ctx) > 0.5;
	}

	@Override
	public boolean verify(BuilderContext ctx) {
		return exp.verify(ctx);
	}
}
