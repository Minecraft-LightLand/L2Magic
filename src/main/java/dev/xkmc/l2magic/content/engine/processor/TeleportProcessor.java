package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public record TeleportProcessor(
        DoubleVariable x,
        DoubleVariable y,
        DoubleVariable z
) implements EntityProcessor<TeleportProcessor> {

    public static final Codec<TeleportProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
            DoubleVariable.codec("x", e -> e.x),
            DoubleVariable.codec("y", e -> e.y),
            DoubleVariable.codec("z", e -> e.z)
    ).apply(i, TeleportProcessor::new));

    @Override
    public ProcessorType<TeleportProcessor> type() {
        return EngineRegistry.TP.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        double posX = x().eval(ctx);
        double posY = y().eval(ctx);
        double posZ = z().eval(ctx);
        for (var e : le) {
            e.teleportTo(posX, posY, posZ);
        }
    }
}
