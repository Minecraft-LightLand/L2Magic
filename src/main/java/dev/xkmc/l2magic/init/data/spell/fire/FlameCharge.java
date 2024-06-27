package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2complements.init.registrate.LCEffects;
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
import dev.xkmc.l2magic.init.data.spell.UnrealHelper;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class FlameCharge extends SpellDataGenEntry {

    public static final ResourceKey<SpellAction> FLAME_CHARGE = spell("flame_charge");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(FLAME_CHARGE.location()), "Flame Charge");
    }

    @Override
    public void register(BootstapContext<SpellAction> ctx) {
        new SpellAction(
                flameCharge(new DataGenContext(ctx)),
                Items.FIRE_CHARGE, 3300,
                SpellCastType.CHARGE,
                SpellTriggerType.FACING_FRONT
        ).verifyOnBuild(ctx, FLAME_CHARGE);
    }

    private static ConfiguredEngine<?> flameCharge(DataGenContext ctx) {
        return new PredicateLogic(BooleanVariable.of("Power==0"),
                new SphereRandomIterator(
                        DoubleVariable.of("2"),
                        IntVariable.of("50"),
                        new DustParticleInstance(
                                ColorVariable.Static.of(0xFF0000),
                                DoubleVariable.of("1"),
                                DoubleVariable.of("-.2"),
                                IntVariable.of("10")
                        ),
                        null
                ),
                new DelayedIterator(
                        IntVariable.of("min(2*TickUsing,80)"),
                        IntVariable.of("1"),
                        new MoveEngine(
                                List.of(
                                        new ToCurrentCasterPosModifier(),
                                        new SetDirectionModifier(
                                                DoubleVariable.ZERO,
                                                DoubleVariable.of("1"),
                                                DoubleVariable.ZERO
                                        ),
                                        new ForwardOffsetModifier(DoubleVariable.of("1")),
                                        new ToCurrentCasterDirModifier()
                                ),
                                new ListLogic(List.of(
                                        new ProcessorEngine(  // Push
                                                SelectionType.ALL,
                                                new SelfSelector(),
                                                List.of(
                                                        new PushProcessor(
                                                                DoubleVariable.of(".2"),
                                                                DoubleVariable.ZERO,
                                                                DoubleVariable.ZERO,
                                                                PushProcessor.Type.UNIFORM
                                                        )
                                                )
                                        ),
                                        new ProcessorEngine(  // Damage
                                                SelectionType.ENEMY,
                                                new ApproxCylinderSelector(
                                                        DoubleVariable.of("2"),
                                                        DoubleVariable.of("2")
                                                ),
                                                List.of(
                                                        new DamageProcessor(ctx.damage(DamageTypes.FIREBALL),
                                                                DoubleVariable.of("4"), true, true),
                                                        new KnockBackProcessor(
                                                                DoubleVariable.of("0.2"),
                                                                DoubleVariable.ZERO,
                                                                DoubleVariable.ZERO
                                                        )
                                                )
                                        ),
                                        new MoveEngine(  // Render
                                                List.of(
                                                        new ForwardOffsetModifier(DoubleVariable.of("-1"))
                                                ),
                                                UnrealHelper.cone(
                                                        1.5,
                                                        3,
                                                        30,
                                                        30,
                                                        new MoveEngine(
                                                                List.of(
                                                                        new ToCurrentCasterDirModifier()
                                                                ),
                                                                new DustParticleInstance(
                                                                        ColorVariable.Static.of(0xFF0000),
                                                                        DoubleVariable.of(".5"),
                                                                        DoubleVariable.of("1"),
                                                                        IntVariable.of("1")
                                                                )
                                                        )
                                                )
                                        )
                                ))
                        ),
                null
        )
        );
    }

}
