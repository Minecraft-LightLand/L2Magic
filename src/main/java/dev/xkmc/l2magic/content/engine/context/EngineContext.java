package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.events.ClientEventHandler;
import dev.xkmc.shadow.objecthunter.exp4j.Expression;
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

	public double eval(Expression exp) {
		exp.setVariables(parameters);
		if (user.scheduler() != null)
			exp.setVariable("Time", user.scheduler().time);
		return exp.evaluate();
	}

	public void schedule(int tick, Runnable o) {
		var sche = user.scheduler();
		if (sche == null) throw new IllegalStateException("Scheduler is not present!");
		sche.schedule(tick, o);
	}

	public RandomSource nextRand() {
		return RandomSource.create(rand.nextLong());
	}

	public void registerScheduler() {
		var sche = user.scheduler();
		if (sche == null) return;
		if (!sche.isFinished()) {
			if (user().level().isClientSide())
				ClientEventHandler.schedulePersistent(sche::tick);
			else GeneralEventHandler.schedulePersistent(sche::tick);
		}
	}
}
