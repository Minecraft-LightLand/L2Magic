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
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
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
                new DelayedIterator(  // core
                        IntVariable.of("24"),
                        IntVariable.of("5"),
                        new SphereRandomIterator(
                                DoubleVariable.of("0.1+0.005*r"),
                                IntVariable.of("50"),
                                new DustParticleInstance(
                                        ColorVariable.Static.of(0xE88B00),
                                        DoubleVariable.of("2"),
                                        DoubleVariable.of(".01"),
                                        IntVariable.of("10")
                                ),
                                null
                        ),
                        "r"
                ),
                new LoopIterator(  // waves
                        IntVariable.of("6"),
                        new DelayLogic(
                                IntVariable.of("10+30*t-2*t*t"),  // 10 + (0-100), 6 waves
                                new SphereRandomIterator(
                                        DoubleVariable.of("8"),
                                        IntVariable.of("200"),
                                        new DustParticleInstance(
                                                ColorVariable.Static.of(0xE88B00),
                                                DoubleVariable.of("2"),
                                                DoubleVariable.of("-.5"),
                                                IntVariable.of("20-2*t")
                                        ),
                                        null
                                )
                        ),
                        "t"
                ),
                new DelayLogic(  // explode
                        IntVariable.of("120"),
                        new ListLogic(List.of(
                                new SphereRandomIterator(
                                        DoubleVariable.of(".01"),
                                        IntVariable.of("200"),
                                        new DustParticleInstance(
                                                ColorVariable.Static.of(0xE88B00),
                                                DoubleVariable.of("2"),
                                                DoubleVariable.of("3"),
                                                IntVariable.of("10")
                                        ),
                                        null
                                ),
                                new ProcessorEngine(
                                        SelectionType.ENEMY,
                                        new BoxSelector(
                                                DoubleVariable.of("8"),  // Only have range explode
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
                                                        DoubleVariable.of("1"),
                                                        DoubleVariable.ZERO,
                                                        DoubleVariable.ZERO,
                                                        PushProcessor.Type.TO_CENTER
                                                )
                                        )
                                )
                        ))
                ),
                new DelayedIterator(  // tick damage
                        IntVariable.of("120"),
                        IntVariable.of("1"),
                        new ProcessorEngine(
                                SelectionType.ENEMY,
                                new BoxSelector(
                                        DoubleVariable.of("16"),  // full range drain
                                        DoubleVariable.of("16"),
                                        true
                                ),
                                List.of(
                                        new DamageProcessor(
                                                ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                                DoubleVariable.of("1"),
                                                true, false
                                        ),
                                        new PushProcessor(
                                                DoubleVariable.of("-.1"),
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
