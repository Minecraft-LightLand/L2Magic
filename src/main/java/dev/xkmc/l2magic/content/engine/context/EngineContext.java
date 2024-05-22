package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.shadow.objecthunter.exp4j.Expression;
import dev.xkmc.shadow.objecthunter.exp4j.ExpressionBuilder;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public record EngineContext(UserContext user, LocationContext loc, RandomSource rand, Map<String, Double> parameters) {

	public EngineContext with(LocationContext modified) {
		return new EngineContext(user, modified, rand, parameters);
	}

	public void iterateOn(LocationContext loc, @Nullable String index, int i, ConfiguredEngine<?> child) {
		if (index == null || index.isEmpty()) {
			execute(loc, child);
			return;
		}
		var param = new LinkedHashMap<>(parameters);
		param.put(index, (double) i);
		execute(loc, param, child);
	}

	public void execute(LocationContext loc, ConfiguredEngine<?> child) {
		execute(loc, parameters, child);
	}

	public void execute(ConfiguredEngine<?> child) {
		execute(loc, parameters, child);
	}

	public void execute(LocationContext loc, Map<String, Double> parameters, ConfiguredEngine<?> child) {
		child.execute(new EngineContext(user, loc, nextRand(), parameters));
	}

	public double eval(String val) {
		return new ExpressionBuilder(val).variables(parameters.keySet())
				.build().setVariables(parameters).evaluate();
	}

	public double eval(Expression exp) {
		return exp.setVariables(parameters).evaluate();
	}

	public void schedule(int tick, Runnable o) {
		user.scheduler().schedule(tick, o);
	}

	public RandomSource nextRand() {
		return RandomSource.create(rand.nextLong());
	}

}
