package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashSet;
import java.util.Set;

public record SelfSelector() implements EntitySelector<SelfSelector> {

	public static Codec<SelfSelector> CODEC = Codec.unit(new SelfSelector());

	@Override
	public SelectorType<SelfSelector> type() {
		return EngineRegistry.SELF.get();
	}

	@Override
	public LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx, SelectionType type) {
		return new LinkedHashSet<>(Set.of(ctx.user().user()));
	}

}
