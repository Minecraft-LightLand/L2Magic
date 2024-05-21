package dev.xkmc.l2magic.content.engine.variable;

import dev.xkmc.l2magic.content.engine.context.BuilderContext;

public interface NumericVariable extends Variable {

	ExpressionHolder exp();

	@Override
	default boolean verify(BuilderContext ctx) {
		return exp().verify(ctx);
	}

}
