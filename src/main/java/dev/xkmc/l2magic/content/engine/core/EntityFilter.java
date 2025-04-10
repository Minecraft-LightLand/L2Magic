package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

public interface EntityFilter<T extends Record & EntityFilter<T>> extends Verifiable {

	Codec<EntityFilter<?>> CODEC = EngineRegistry.FILTER.codec()
			.dispatch(EntityFilter::type, FilterType::codec);

	FilterType<T> type();

	boolean test(LivingEntity le, EngineContext ctx);

}
