package dev.xkmc.l2magic.content.engine.variable;

import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.shadow.objecthunter.exp4j.Expression;
import dev.xkmc.shadow.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionHolder {

	public static ExpressionHolder of(String str) {
		return new ExpressionHolder(str);
	}

	private final String str;

	private Expression exp;
	private boolean isConstant;
	private double constant;

	public ExpressionHolder(String str) {
		this.str = str;
	}


	public double eval(EngineContext ctx) {
		if (isConstant) {
			return constant;
		}
		if (exp == null) throw new IllegalStateException("Must call verify before fetching expression");
		return ctx.eval(exp);
	}

	public boolean verify(BuilderContext ctx) {
		try {
			exp = new ExpressionBuilder(str).variables(ctx.params()).build();
		} catch (Exception e) {
			ctx.error(str, e);
			return false;
		}
		if (exp.validate(true).isValid()) {
			isConstant = true;
			constant = exp.evaluate();
		}
		return true;
	}

}
