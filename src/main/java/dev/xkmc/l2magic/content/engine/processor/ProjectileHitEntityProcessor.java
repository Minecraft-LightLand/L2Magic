package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;

public record ProjectileHitEntityProcessor(
) implements EntityProcessor<ProjectileHitEntityProcessor> {

	public static final MapCodec<ProjectileHitEntityProcessor> CODEC = MapCodec.unit(ProjectileHitEntityProcessor::new);

	@Override
	public ProcessorType<ProjectileHitEntityProcessor> type() {
		return EngineRegistry.PROJECTILE_HIT.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		var user = ctx.user().user();
		var source = user.damageSources().mobProjectile(null, user);
		for (var e : le.entries()) {
			if (e.root() instanceof LivingEntity) continue;
			e.root().hurt(source, 1);
		}
	}

	@Override
	public boolean serverOnly() {
		return true;
	}
}
