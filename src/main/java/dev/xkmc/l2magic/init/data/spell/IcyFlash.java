package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetNormalModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.*;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.selector.SelfSelector;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class IcyFlash extends SpellDataGenEntry {

    public static final ResourceKey<SpellAction> ICY_FLASH = spell("icy_flash");

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add(SpellAction.lang(ICY_FLASH.location()), "Icy Flash");
    }

    @Override
    public void register(BootstapContext<SpellAction> ctx) {
        new SpellAction(
                icyFlash(new DataGenContext(ctx)),
                Items.SNOWBALL.asItem(), 3100,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, ICY_FLASH);
    }

    private static ConfiguredEngine<?> icyFlash(DataGenContext ctx) {
        return new ListLogic(List.of(
                new ProcessorEngine(  // TP
                        SelectionType.ALL,
                        new SelfSelector(),
                        List.of(
                                new TeleportProcessor(
                                        DoubleVariable.of("PosX"),
                                        DoubleVariable.of("PosY"),
                                        DoubleVariable.of("PosZ")
                                )
                        )
                ),
                new SoundInstance(  // Sound
                        SoundEvents.ENDERMAN_TELEPORT,
                        DoubleVariable.of("2"),
                        DoubleVariable.ZERO
                ),
                new ProcessorEngine(  // Damage
                        SelectionType.ENEMY,
                        new ApproxCylinderSelector(
                                DoubleVariable.of("1"),
                                DoubleVariable.of("2")
                        ),
                        List.of(
                                new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
                                        DoubleVariable.of("4"), true, true),
                                new KnockBackProcessor(
                                        DoubleVariable.of("0.1"),
                                        DoubleVariable.ZERO,
                                        DoubleVariable.ZERO
                                ),
                                new EffectProcessor(
                                        LCEffects.ICE.get(),
                                        IntVariable.of("100"),
                                        IntVariable.of("0"),
                                        false, false
                                )
                        )
                ),
                new LoopIterator(  // Render
                        IntVariable.of("100"),
                        new MoveEngine(
                                List.of(
                                        new RandomOffsetModifier(
                                                RandomOffsetModifier.Type.RECT,
                                                DoubleVariable.of("2"),
                                                DoubleVariable.of("2"),
                                                DoubleVariable.of("2")
                                        ),
                                        new SetDirectionModifier(
                                                DoubleVariable.ZERO,
                                                DoubleVariable.of("-1"),
                                                DoubleVariable.ZERO
                                        )
                                ),
                                new SimpleParticleInstance(
                                        ParticleTypes.SNOWFLAKE,
                                        DoubleVariable.of("0.1")
                                )
                        ),
                        null
                )
        ));
    }
}
