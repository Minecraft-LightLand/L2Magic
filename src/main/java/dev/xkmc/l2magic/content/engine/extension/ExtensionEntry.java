package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2magic.content.engine.context.AnalyticContext;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.init.L2Magic;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class ExtensionEntry<E extends ExtensionEntry<E, K>, K extends ExtensionKey<E, K>> {

	protected final Verifiable entry;
	protected final Set<Verifiable> iterated = new HashSet<>();

	public ExtensionEntry(Verifiable entry) {
		this.entry = entry;
	}

	protected abstract boolean match(K e);

	protected abstract void initAnalysis();

	protected abstract void process(IExtended<?> p);

	public void analyze() {
		iterated.clear();
		initAnalysis();
		EngineHelper.analyze(entry, new AnalyticContext("", this::check), entry.getClass());
	}

	protected void check(String s, @Nullable Verifiable v) {
		switch (v) {
			case CustomProjectileShoot p -> {
				if (iterated.contains(p)) return;
				iterated.add(p);
				var proj = p.config().value();
				Optional.ofNullable(proj.tick()).ifPresent(e -> EngineHelper.analyze(e, new AnalyticContext("", this::check), e.getClass()));
				Optional.ofNullable(proj.land()).ifPresent(e -> EngineHelper.analyze(e, new AnalyticContext("", this::check), e.getClass()));
				Optional.ofNullable(proj.expire()).ifPresent(e -> EngineHelper.analyze(e, new AnalyticContext("", this::check), e.getClass()));
				for (var hit : proj.hit())
					EngineHelper.analyze(hit, new AnalyticContext("", this::check), hit.getClass());
			}
			case IExtended<?> p -> process(p);
			case null -> L2Magic.LOGGER.error("Null input not allowed for path {}", s);
			default -> {
			}
		}
	}


}
