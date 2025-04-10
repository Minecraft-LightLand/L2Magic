package dev.xkmc.l2magic.content.engine.variable;

import dev.xkmc.shadow.objecthunter.exp4j.function.Function;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class DefaultFunctions {

	public static List<Function> FUNCTIONS = new ArrayList<>();

	static {
		FUNCTIONS.addAll(List.of(
				of("min", Math::min),
				of("max", Math::max),
				of("cosDegree", e -> Math.cos(e * Mth.DEG_TO_RAD)),
				of("sinDegree", e -> Math.sin(e * Mth.DEG_TO_RAD))
		));
	}

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

	private static Function of(String id, UniOp func) {
		return new Function(id, 1) {
			@Override
			public double apply(double... args) {
				return func.apply(args[0]);
			}
		};
	}

	private interface Func {

		double apply(double a, double b);

	}

	private interface UniOp {

		double apply(double a);

	}

}
