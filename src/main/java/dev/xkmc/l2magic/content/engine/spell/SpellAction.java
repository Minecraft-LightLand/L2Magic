package dev.xkmc.l2magic.content.engine.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2magic.content.engine.context.*;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.events.ClientEventHandler;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public record SpellAction(ConfiguredEngine<?> action, Item icon,
						  SpellCastType castType, SpellTriggerType triggerType) {

	private static final Codec<SpellCastType> CAST_CODEC = EngineHelper.enumCodec(SpellCastType.class, SpellCastType.values());
	private static final Codec<SpellTriggerType> TRIGGER_CODEC = EngineHelper.enumCodec(SpellTriggerType.class, SpellTriggerType.values());

	public static final Codec<SpellAction> CODEC = RecordCodecBuilder.create(i -> i.group(
			ConfiguredEngine.codec("action", SpellAction::action),
			ForgeRegistries.ITEMS.getCodec().fieldOf("icon").forGetter(e -> e.icon),
			CAST_CODEC.fieldOf("cast_type").forGetter(e -> e.castType),
			TRIGGER_CODEC.fieldOf("trigger_type").forGetter(e -> e.triggerType)
	).apply(i, SpellAction::new));

	public Set<String> params() {
		return SpellContext.DEFAULT_PARAMS;
	}

	public void execute(SpellContext ctx) {
		var sche = new Scheduler();
		try {
			var source = new SingleThreadedRandomSource(ctx.seed());
			var normal = triggerType == SpellTriggerType.FACING_FRONT ||
					triggerType == SpellTriggerType.FACING_BACK ?
					Orientation.fromForward(ctx.facing()).normal() :
					LocationContext.UP;
			action().execute(new EngineContext(
					new UserContext(ctx.user().level(), ctx.user(), sche),
					new LocationContext(ctx.origin(), ctx.facing(), normal),
					source, ctx.defaultArgs()
			));
		} catch (Exception e) {
			L2Magic.LOGGER.throwing(e);
			return;
		}
		if (!sche.isFinished()) {
			if (ctx.user().level().isClientSide())
				ClientEventHandler.schedulePersistent(sche::tick);
			else GeneralEventHandler.schedulePersistent(sche::tick);
		}
		if (!ctx.user().level().isClientSide()) {
			L2Magic.HANDLER.toTrackingPlayers(new SpellUsePacket(this, ctx), ctx.user());
		}
	}

	public boolean verify(ResourceLocation id) {
		return action().verify(new BuilderContext(L2Magic.LOGGER, id.toString(), params()));
	}

	public void verifyOnBuild(BootstapContext<SpellAction> ctx, ResourceKey<SpellAction> id) {
		verify(id.location());
		ctx.register(id, this);
	}

}
