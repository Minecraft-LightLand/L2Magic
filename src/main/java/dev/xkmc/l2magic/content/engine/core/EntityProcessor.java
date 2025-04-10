package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2magic.content.engine.logic.DelayLogic;
import dev.xkmc.l2magic.content.engine.processor.DelayedProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public interface EntityProcessor<T extends Record & EntityProcessor<T>>
		extends Verifiable, IExtended<T> {

	Codec<EntityProcessor<?>> CODEC = EngineRegistry.PROCESSOR.codec()
			.dispatch(EntityProcessor::type, ProcessorType::codec);

	ProcessorType<T> type();

	void process(SelectedEntities le, EngineContext ctx);

	boolean serverOnly();

	@DataGenOnly
	@SuppressWarnings("deprecation")
	default EntityProcessor<?> delay(IntVariable delay) {
		return new DelayedProcessor(delay, this);
	}

}
