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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class MagnetCore extends SpellDataGenEntry {

    public static final ResourceKey<SpellAction> MAGNET_CORE = spell("magnet_core");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(MAGNET_CORE.location()), "Magnet Core");
    }

    @Override
    public void register(BootstapContext<SpellAction> ctx) {
        new SpellAction(
                magnetCore(new DataGenContext(ctx)),
                Blocks.IRON_ORE.asItem(), 2000,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, MAGNET_CORE);
    }

    private static ConfiguredEngine<?> magnetCore(DataGenContext ctx) {
        return new ListLogic(List.of(
                new DelayedIterator(
                        IntVariable.of("40"),
                        IntVariable.of("2"),
                        new SphereRandomIterator(
                                DoubleVariable.of("0.2+0.01*r"),
                                IntVariable.of("20"),
                                new DustParticleInstance(
                                        ColorVariable.Static.of(0xE88B00),
                                        DoubleVariable.of("2+0.04*r"),
                                        DoubleVariable.ZERO
                                ),
                                null
                        ),
                        "r"
                ),
                new LoopIterator(
                        IntVariable.of("5"),
                        new DelayLogic(
                                IntVariable.of("10+30*t-2*t*t"),
                                new SphereRandomIterator(
                                        DoubleVariable.of("8"),
                                        IntVariable.of("200"),
                                        new SimpleParticleInstance(
                                                ParticleTypes.SQUID_INK,
                                                DoubleVariable.of("-.5")
                                        ),
                                        null
                                )
                        ),
                        "t"
                ),
                new DelayLogic(
                        IntVariable.of("115"),
                        new ListLogic(List.of(
                                new SphereRandomIterator(
                                        DoubleVariable.of(".01"),
                                        IntVariable.of("200"),
                                        new SimpleParticleInstance(
                                                ParticleTypes.SQUID_INK,
                                                DoubleVariable.of("3")
                                        ),
                                        null
                                ),
                                new ProcessorEngine(
                                        SelectionType.ENEMY,
                                        new BoxSelector(
                                                DoubleVariable.of("8"),
                                                DoubleVariable.of("8"),
                                                true
                                        ),
                                        List.of(
                                            new DamageProcessor(
                                                    ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                                    DoubleVariable.of("20"),
                                                    true, false
                                            ),
                                            new PushProcessor(
                                                    DoubleVariable.of(".5"),
                                                    DoubleVariable.ZERO,
                                                    DoubleVariable.ZERO,
                                                    PushProcessor.Type.TO_CENTER
                                            )
                                        )
                                )
                        ))
                ),
                new DelayedIterator(
                        IntVariable.of("115"),
                        IntVariable.of("1"),
                        new ProcessorEngine(
                                SelectionType.ENEMY,
                                new BoxSelector(
                                        DoubleVariable.of("8"),
                                        DoubleVariable.of("8"),
                                        true
                                ),
                                List.of(
                                        new DamageProcessor(
                                                ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                                DoubleVariable.of(".1"),
                                                true, false
                                        ),
                                        new PushProcessor(
                                                DoubleVariable.of("-.05"),
                                                DoubleVariable.ZERO,
                                                DoubleVariable.ZERO,
                                                PushProcessor.Type.TO_CENTER
                                        )
                                )
                        ),
                        null
                )
        ));
    }

}
