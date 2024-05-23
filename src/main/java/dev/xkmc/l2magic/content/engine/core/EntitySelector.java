package dev.xkmc.l2magic.content.engine.core;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashSet;

public interface EntitySelector<T extends Record & EntitySelector<T>> extends Verifiable {

	Codec<EntitySelector<?>> CODEC = EngineRegistry.SELECTOR.codec()
			.dispatch(EntitySelector::type, SelectorType::codec);

	SelectorType<T> type();

	LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx, SelectionType type);

}
