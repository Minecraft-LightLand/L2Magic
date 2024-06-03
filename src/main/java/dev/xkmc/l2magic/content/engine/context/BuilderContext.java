package dev.xkmc.l2magic.content.engine.context;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record BuilderContext(Logger logger, String path, Set<String> params, boolean hasScheduler) {

	public static BuilderContext withScheduler(Logger logger, String path, Set<String> params) {
		return new BuilderContext(logger, path, Sets.union(params, Set.of("Time")), true);
	}

	public static BuilderContext instant(Logger logger, String path, Set<String> params) {
		return new BuilderContext(logger, path, params, false);
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
