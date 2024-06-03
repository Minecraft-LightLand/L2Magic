package dev.xkmc.l2magic.init.data.spell;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;

import java.util.List;

public class UnrealHelper {
    public static ConfiguredEngine<?> cone(double radius, double height, int num, int layers, ConfiguredEngine<?> child) {
        return new LoopIterator(
                IntVariable.of(Integer.toString(layers)),
                new MoveEngine(
                        List.of(
                                new SetDirectionModifier(
                                        DoubleVariable.ZERO,
                                        DoubleVariable.of("1"),
                                        DoubleVariable.ZERO
                                ),
                                new ForwardOffsetModifier(
                                        DoubleVariable.of(height/(layers-1) + "*cone_l")
                                ),
                                new Dir2NormalModifier()
                        ),
                        new RingIterator(
                                DoubleVariable.of(radius/(layers-1) + String.format("*(%d-cone_l)", layers)),
                                DoubleVariable.of("0"),
                                DoubleVariable.of("360"),
                                IntVariable.of(1D*num/(layers-1) + String.format("*(%d-cone_l)", layers)),
                                false,
                                child,
                                null
                        )
                ),
                "cone_l"
        );
    }
}
