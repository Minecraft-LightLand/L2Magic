package dev.xkmc.l2magic.content.engine.context;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record BuilderContext(Logger logger, String path, Set<String> params) {

	public void error(String str) {
		logger.error(path + ": " + str);
	}

	public void error(String str, Exception e) {
		logger.error(path + ": " + str);
		logger.throwing(e);
	}

	public BuilderContext of(String s) {
		return new BuilderContext(logger, path + "/" + s, params);
	}

	public BuilderContext of(String s, @Nullable Set<String> params) {
		if (params == null) return of(s);
		return new BuilderContext(logger, path + "/" + s, Sets.union(params(), params));
	}

}
