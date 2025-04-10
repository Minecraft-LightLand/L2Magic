package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2core.events.ClientScheduler;
import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.shadow.objecthunter.exp4j.Expression;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public record EngineContext(UserContext user, LocationContext loc, RandomSource rand, Map<String, Double> parameters) {

	public EngineContext with(LocationContext modified) {
		return new EngineContext(user, modified, rand, parameters);
	}

	public EngineContext withParam(String key, double val) {
		var ans = new LinkedHashMap<>(parameters);
		ans.put(key, val);
		return new EngineContext(user, loc, rand, ans);
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
		var param = new LinkedHashMap<>(this.parameters);
		param.putAll(parameters);
		child.execute(new EngineContext(user, loc, nextRand(), param));
	}

	public boolean test(IPredicate test) {
		return test.test(new EngineContext(user, loc, nextRand(), parameters));
	}

	public boolean test(LocationContext loc, @Nullable String index, int i, IPredicate pred) {
		if (index == null || index.isEmpty()) {
			return pred.test(new EngineContext(user, loc, nextRand(), parameters));
		}
		var param = new LinkedHashMap<>(parameters);
		param.put(index, (double) i);
		return pred.test(new EngineContext(user, loc, nextRand(), param));
	}

	public boolean test(LocationContext loc, Map<String, Double> parameters, IPredicate pred) {
		var param = new LinkedHashMap<>(this.parameters);
		param.putAll(parameters);
		return pred.test(new EngineContext(user, loc, nextRand(), param));
	}


	public void process(SelectedEntities entities, EntityProcessor<?> action) {
		action.process(entities, new EngineContext(user, loc, nextRand(), parameters));
	}

	public double eval(Expression exp) {
		exp.setVariables(parameters);
		if (user.scheduler() != null)
			exp.setVariable("Time", user.scheduler().time);
		exp.setVariable("PosX", loc.pos().x);
		exp.setVariable("PosY", loc.pos().y);
		exp.setVariable("PosZ", loc.pos().z);
		exp.setVariable("CasterX", user.user().getX());
		exp.setVariable("CasterY", user.user().getY());
		exp.setVariable("CasterZ", user.user().getZ());
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
				ClientScheduler.schedulePersistent(sche::tick);
			else SchedulerHandler.schedulePersistent(sche::tick);
		}
	}

}
