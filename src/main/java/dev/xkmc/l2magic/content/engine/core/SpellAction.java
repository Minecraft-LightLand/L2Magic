package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2magic.content.engine.context.*;
import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public record SpellAction(ConfiguredEngine<?> action) {

	public static final Codec<SpellAction> CODEC = RecordCodecBuilder.create(i -> i.group(
			ConfiguredEngine.codec("action", SpellAction::action)
	).apply(i, SpellAction::new));

	public Set<String> params() {
		return SpellContext.DEFAULT_PARAMS;
	}

	public void execute(SpellContext ctx) {
		var sche = new Scheduler();
		action().execute(new EngineContext(
				new UserContext(ctx.user().level(), ctx.user(), ctx.user().getRandom(), sche),
				new LocationContext(ctx.origin(), ctx.facing(), LocationContext.UP),
				ctx.defaultArgs()
		));
		if (!sche.isFinished()) {
			GeneralEventHandler.schedulePersistent(sche::tick);
		}
	}

	public boolean verify(ResourceLocation id) {
		return action().verify(new BuilderContext(L2Magic.LOGGER, id.toString(), params()));
	}

}
