package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record PropertyProcessor(
		Type property,
		IntVariable duration
) implements SimpleServerProcessor<PropertyProcessor> {

	public enum Type {
		IGNITE(Entity::getRemainingFireTicks, Entity::setRemainingFireTicks),
		FREEZE(Entity::getTicksFrozen, Entity::setTicksFrozen);

		private final Function<Entity, Integer> getter;
		private final BiConsumer<Entity, Integer> func;

		Type(Function<Entity, Integer> getter, BiConsumer<Entity, Integer> func) {
			this.getter = getter;
			this.func = func;
		}

		public void set(Entity e, int dur) {
			if (getter.apply(e) < dur)
				func.accept(e, dur);
		}

		public PropertyProcessor of(String duration) {
			return new PropertyProcessor(this, IntVariable.of(duration));
		}

	}

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final MapCodec<PropertyProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			TYPE_CODEC.fieldOf("property").forGetter(e -> e.property),
			IntVariable.codec("duration", e -> e.duration)
	).apply(i, PropertyProcessor::new));

	@Override
	public ProcessorType<PropertyProcessor> type() {
		return EngineRegistry.PROP.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		int dur = duration.eval(ctx);
		for (var e : le.entries()) {
			property.set(e.root(), dur);
		}
	}

}
