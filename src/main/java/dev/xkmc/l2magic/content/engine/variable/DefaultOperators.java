package dev.xkmc.l2magic.content.engine.variable;

import dev.xkmc.shadow.objecthunter.exp4j.operator.Operator;

public class DefaultOperators {

	public static Operator[] OPERATORS = {
			of("<", 100, (a, b) -> a < b),
			of("<=", 100, (a, b) -> a <= b),
			of(">", 100, (a, b) -> a > b),
			of(">=", 100, (a, b) -> a >= b),
			of("==", 100, (a, b) -> a == b),
			of("!=", 100, (a, b) -> a != b),
			of("&", 70, (a, b) -> a > 0.5 && b > 0.5),
			of("|", 50, (a, b) -> a > 0.5 || b > 0.5)
	};

	private static Operator of(String id, int precedent, Func func) {
		return new Operator(id, 2, false, precedent) {
			@Override
			public double apply(double... args) {
				return func.apply(args[0], args[1]) ? 1 : 0;
			}
		};
	}

	private interface Func {
		boolean apply(double a, double b);
	}


}
