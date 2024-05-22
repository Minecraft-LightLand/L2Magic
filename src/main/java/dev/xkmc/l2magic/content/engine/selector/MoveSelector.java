package dev.xkmc.l2magic.content.engine.selector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntitySelector;
import dev.xkmc.l2magic.content.engine.core.Modifier;
import dev.xkmc.l2magic.content.engine.core.SelectorType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedHashSet;
import java.util.List;

public record MoveSelector(List<Modifier<?>> modifiers, EntitySelector<?> child)
		implements EntitySelector<MoveSelector> {

	public static final Codec<MoveSelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(Modifier.CODEC).fieldOf("modifiers").forGetter(e -> e.modifiers),
			EntitySelector.CODEC.fieldOf("child").forGetter(e -> e.child)
	).apply(i, MoveSelector::new));

	@Override
	public SelectorType<MoveSelector> type() {
		return EngineRegistry.MOVE_SELECTOR.get();
	}

	@Override
	public LinkedHashSet<LivingEntity> find(ServerLevel sl, EngineContext ctx) {
		for (var e : modifiers) {
			ctx = ctx.with(e.modify(ctx));
		}
		return child().find(sl, ctx);
	}

}