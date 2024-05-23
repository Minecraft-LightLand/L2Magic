package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public interface EntityProcessor<T extends Record & EntityProcessor<T>> extends Verifiable {

	Codec<EntityProcessor<?>> CODEC = EngineRegistry.PROCESSOR.codec()
			.dispatch(EntityProcessor::type, ProcessorType::codec);

	ProcessorType<T> type();

	void process(Collection<LivingEntity> le, EngineContext ctx);

}
