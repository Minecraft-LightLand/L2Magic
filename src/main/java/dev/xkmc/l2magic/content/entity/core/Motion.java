package dev.xkmc.l2magic.content.entity.core;

import com.mojang.serialization.Codec;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.phys.Vec3;

public interface Motion<T extends Record & Motion<T>> extends Verifiable {

	Codec<Motion<?>> CODEC = EngineRegistry.MOTION.codec()
			.dispatch(Motion::type, MotionType::codec);

	MotionType<T> type();

	ProjectileMovement move(EngineContext ctx, Vec3 vec, Vec3 pos);

}
