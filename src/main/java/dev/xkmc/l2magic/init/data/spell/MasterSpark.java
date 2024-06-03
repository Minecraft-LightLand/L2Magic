package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.*;
import dev.xkmc.l2magic.content.engine.processor.*;
import dev.xkmc.l2magic.content.engine.selector.*;
import dev.xkmc.l2magic.content.engine.spell.*;
import dev.xkmc.l2magic.content.engine.variable.*;
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
                                        new DustParticleInstance(
                                                ColorVariable.Static.of(0xE88B00),
                                                DoubleVariable.of("2"),
                                                DoubleVariable.of("-2")
                                        ),
                                        null
                                ),
                                null
                        ),
                        new PredicateLogic(
                                BooleanVariable.of("TickUsing>50"),
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
                                                                IntVariable.of("50"),
                                                                new RandomVariableLogic("r", 1,
                                                                        new MoveEngine(List.of(RotationModifier.of("0", "105-30*r0")),
                                                                                new SimpleParticleInstance(
                                                                                        ParticleTypes.END_ROD,
                                                                                        DoubleVariable.of("3")
                                                                                )
                                                                        )
                                                                ),
                                                                null
                                                        )
                                                )

                                        )
                                ),
                                null
                        )
                )
        );
    }

}
