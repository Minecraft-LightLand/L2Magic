package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.shadow.objecthunter.exp4j.Expression;
import dev.xkmc.shadow.objecthunter.exp4j.ExpressionBuilder;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public record EngineContext(UserContext user, LocationContext loc, Map<String, Double> parameters) {

	public EngineContext with(Vec3 pos) {
		return new EngineContext(user, loc.with(pos), parameters);
	}

	public EngineContext iterateOn(LocationContext loc, @Nullable String index, int i) {
		if (index == null || index.isEmpty()) {
			return with(loc);
		}
		var param = new LinkedHashMap<>(parameters);
		param.put(index, (double) i);
		return new EngineContext(user, loc, param);
	}

	public EngineContext with(LocationContext loc) {
		return new EngineContext(user, loc, parameters);
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
}
