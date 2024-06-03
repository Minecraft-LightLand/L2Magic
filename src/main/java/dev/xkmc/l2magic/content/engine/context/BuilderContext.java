package dev.xkmc.l2magic.content.engine.context;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record BuilderContext(Logger logger, String path, Set<String> params, boolean hasScheduler) {

	private static final Set<String> DEFAULT_PARAMS = Set.of("PosX", "PosY", "PosZ", "CasterX", "CasterY", "CasterZ");
	private static final Set<String> SCHEDULER_PARAMS = Sets.union(Set.of("Time"), DEFAULT_PARAMS);

	public static BuilderContext withScheduler(Logger logger, String path, Set<String> params) {
		return new BuilderContext(logger, path, Sets.union(params, SCHEDULER_PARAMS), true);
	}

	public static BuilderContext instant(Logger logger, String path, Set<String> params) {
		return new BuilderContext(logger, path, Sets.union(params, DEFAULT_PARAMS), false);
	}

	public void error(String str) {
		logger.error(path + ": " + str);
	}

	public void error(String str, Exception e) {
		logger.error(path + ": " + str);
		logger.throwing(e);
	}

	public BuilderContext of(String s) {
		return new BuilderContext(logger, path + "/" + s, params, hasScheduler);
	}

	public BuilderContext of(String s, @Nullable Set<String> params) {
		if (params == null) return of(s);
		return new BuilderContext(logger, path + "/" + s, Sets.union(params(), params), hasScheduler);
	}

	public boolean requiresScheduler() {
		if (!hasScheduler){
			error("Scheduler is not present for this context. You should not use delay blocks.");
			return false;
		}
		return true;
	}
}
