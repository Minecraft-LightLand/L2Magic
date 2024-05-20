package dev.xkmc.l2magic.content.engine.variable;

import dev.xkmc.shadow.objecthunter.exp4j.function.Function;

import java.util.function.DoubleSupplier;

public class DefaultFunctions {

	public static Function[] FUNCTIONS = {
			of("min", Math::min),
			of("max", Math::max)
	};

	public static Function rand(DoubleSupplier r) {
		return of("rand", (a, b) -> r.getAsDouble() * (b - a) + a);
	}

	private static Function of(String id, Func func) {
		return new Function(id, 2) {
			@Override
			public double apply(double... args) {
				return func.apply(args[0], args[1]);
			}
		};
	}

	private interface Func {

		double apply(double a, double b);

	}

}
