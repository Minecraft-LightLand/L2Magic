package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class MasterSpark extends SpellDataGenEntry {

    public static final ResourceKey<SpellAction> MASTER_SPARK = spell("master_spark");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(MASTER_SPARK.location()), "Master Spark");
    }

    @Override
    public void register(BootstapContext<SpellAction> ctx) {
        new SpellAction(
                masterSpark(new DataGenContext(ctx)),
                Blocks.BEACON.asItem(), 3000,
                SpellCastType.CONTINUOUS,
                SpellTriggerType.FACING_FRONT
        ).verifyOnBuild(ctx, MASTER_SPARK);
    }

    private static ConfiguredEngine<?> masterSpark(DataGenContext ctx) {
        return new ListLogic(
                List.of(
                        new PredicateLogic(
                                BooleanVariable.of("TickUsing<=40"),
                                new SphereRandomIterator(
                                        DoubleVariable.of("3"),
                                        IntVariable.of("20"),
                                        new SimpleParticleInstance(
                                                ParticleTypes.END_ROD,
                                                DoubleVariable.of("-.2")
                                        ),
                                        null
                                ),
                                null
                        ),
                        new DelayLogic(
                                IntVariable.of("60"),
                                new ListLogic(
                                        List.of(
                                                new ProcessorEngine(SelectionType.ENEMY,
                                                        new LinearCubeSelector(
                                                                IntVariable.of("8"), // dist=16, r=2
                                                                DoubleVariable.of("2")
                                                        ),
                                                        List.of(
                                                                new DamageProcessor(
                                                                        ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                                                        DoubleVariable.of("10"),
                                                                        true, true
                                                                ),
                                                                KnockBackProcessor.of("1")
                                                        )
                                                ),
                                                new MoveEngine(
                                                        List.of(
                                                                OffsetModifier.of("0", "-2", "0"),
                                                                new Dir2NormalModifier()
                                                        ),
                                                        new RingRandomIterator(
                                                                DoubleVariable.ZERO,
                                                                DoubleVariable.of("2"),
                                                                DoubleVariable.of("-180"),
                                                                DoubleVariable.of("180"),
                                                                IntVariable.of("10"),
                                                                new RandomVariableLogic("r", 1,
                                                                        new MoveEngine(List.of(RotationModifier.of("0", "105-30*r0")),
                                                                                new SimpleParticleInstance(
                                                                                        ParticleTypes.END_ROD,
                                                                                        DoubleVariable.of("2")
                                                                                )
                                                                        )
                                                                ),
                                                                null
                                                        )
                                                )

                                        )
                                )
                        )
                )
        );
    }

}
