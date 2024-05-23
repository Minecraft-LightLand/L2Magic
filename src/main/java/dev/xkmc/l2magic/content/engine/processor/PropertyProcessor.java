package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record PropertyProcessor(
		Type property,
		IntVariable duration
) implements EntityProcessor<PropertyProcessor> {

	public enum Type {
		IGNITE(Entity::getRemainingFireTicks, Entity::setRemainingFireTicks),
		FREEZE(Entity::getTicksFrozen, Entity::setTicksFrozen);

		private final Function<LivingEntity, Integer> getter;
		private final BiConsumer<LivingEntity, Integer> func;

		Type(Function<LivingEntity, Integer> getter, BiConsumer<LivingEntity, Integer> func) {
			this.getter = getter;
			this.func = func;
		}

		public void set(LivingEntity e, int dur) {
			if (getter.apply(e) < dur)
				func.accept(e, dur);
		}

	}

	private static final Codec<Type> TYPE_CODEC = EngineHelper.enumCodec(Type.class, Type.values());

	public static final Codec<PropertyProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
			TYPE_CODEC.fieldOf("property").forGetter(e -> e.property),
			IntVariable.codec("duration", e -> e.duration)
	).apply(i, PropertyProcessor::new));

	@Override
	public ProcessorType<PropertyProcessor> type() {
		return EngineRegistry.PROP.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		int dur = duration.eval(ctx);
		for (var e : le) {
			property.set(e, dur);
		}
	}

}
