package dev.xkmc.l2magic.content.engine.context;

import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

public record EngineContextData(
		Holder<SpellAction> root, int userId, boolean sche, LocationContext loc, long seed,
		LinkedHashMap<String, Double> param
) {

	public static EngineContextData of(EngineContext ctx) {
		return new EngineContextData(ctx.user().root(), ctx.user().user().getId(), ctx.user().scheduler() != null,
				ctx.loc(), ctx.rand().nextLong(), new LinkedHashMap<>(ctx.parameters()));
	}

	public static EngineContextData of(EngineContext ctx, LocationContext loc) {
		return new EngineContextData(ctx.user().root(), ctx.user().user().getId(), ctx.user().scheduler() != null,
				loc, ctx.rand().nextLong(), new LinkedHashMap<>(ctx.parameters()));
	}

	@Nullable
	public EngineContext create(Level level) {
		if (!(level.getEntity(userId) instanceof LivingEntity le)) return null;
		return new EngineContext(new UserContext(level, le, root, sche ? new Scheduler() : null), loc, RandomSource.create(seed), param);
	}

	public EngineContext remap(EngineContext ctx) {
		return new EngineContext(new UserContext(ctx.user().level(), ctx.user().user(), root, ctx.user().scheduler()), loc, RandomSource.create(seed), param);
	}
}
