package dev.xkmc.l2magic.content.engine.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.*;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.helper.Scheduler;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public record SpellAction(
		ConfiguredEngine<?> action, Item icon, int order,
		SpellCastType castType, SpellTriggerType triggerType,
		@Nullable IPredicate condition
) {

	private static final Codec<SpellCastType> CAST_CODEC = EngineHelper.enumCodec(SpellCastType.class, SpellCastType.values());
	private static final Codec<SpellTriggerType> TRIGGER_CODEC = EngineHelper.enumCodec(SpellTriggerType.class, SpellTriggerType.values());

	public static final Codec<SpellAction> CODEC = RecordCodecBuilder.create(i -> i.group(
			ConfiguredEngine.codec("action", SpellAction::action),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("icon").forGetter(e -> e.icon),
			Codec.INT.fieldOf("order").forGetter(e -> e.order),
			CAST_CODEC.fieldOf("cast_type").forGetter(e -> e.castType),
			TRIGGER_CODEC.fieldOf("trigger_type").forGetter(e -> e.triggerType),
			IPredicate.CODEC.optionalFieldOf("condition").forGetter(e -> Optional.ofNullable(e.condition))
	).apply(i, (a, icon, o,
				c, t, cond) ->
			new SpellAction(a, icon, o, c, t, cond.orElse(null))));

	public static final Codec<Holder<SpellAction>> HOLDER =
			RegistryFileCodec.create(EngineRegistry.SPELL, CODEC, false);

	public SpellAction(
			ConfiguredEngine<?> action, Item icon, int order,
			SpellCastType castType, SpellTriggerType triggerType
	) {
		this(action, icon, order, castType, triggerType, null);
	}

	public static String lang(ResourceLocation rl) {
		return "spell_action." + rl.getNamespace() + "." + rl.getPath();
	}

	public Set<String> params() {
		return SpellContext.DEFAULT_PARAMS;
	}

	public boolean test(Holder<SpellAction> holder, SpellContext ctx) {
		if (condition == null) return true;
		try {
			var source = new SingleThreadedRandomSource(ctx.seed());
			EngineContext engine = new EngineContext(
					new UserContext(ctx.user().level(), ctx.user(), holder, null),
					LocationContext.of(ctx.origin(), ctx.facing()),
					source, ctx.defaultArgs()
			);
			return condition.test(engine);
		} catch (Exception e) {
			L2Magic.LOGGER.throwing(e);
			return false;
		}
	}

	public void execute(Holder<SpellAction> holder, SpellContext ctx) {
		var sche = new Scheduler();
		try {
			var source = new SingleThreadedRandomSource(ctx.seed());
			EngineContext engine = new EngineContext(
					new UserContext(ctx.user().level(), ctx.user(), holder, sche),
					LocationContext.of(ctx.origin(), ctx.facing()),
					source, ctx.defaultArgs()
			);
			action().execute(engine);
			engine.registerScheduler();
		} catch (Exception e) {
			L2Magic.LOGGER.throwing(e);
			return;
		}
		if (!ctx.user().level().isClientSide()) {
			L2Magic.HANDLER.toTrackingPlayers(SpellUsePacket.of(holder, ctx), ctx.user());
		}
	}

	public boolean verify(ResourceLocation id) {
		if (!action().verify(BuilderContext.withScheduler(L2Magic.LOGGER, id.toString(), params())))
			return false;
		if (condition == null) return true;
		return condition.verify(BuilderContext.instant(L2Magic.LOGGER, id + "-condition", params()));
	}

	public void verifyOnBuild(BootstrapContext<SpellAction> ctx, ResourceKey<SpellAction> id) {
		verify(id.location());
		ctx.register(id, this);
	}

	public void verifyOnBuild(BootstrapContext<SpellAction> ctx, DataGenCachedHolder<SpellAction> holder) {
		verify(holder.key.location());
		holder.gen(ctx, this);
	}

}
