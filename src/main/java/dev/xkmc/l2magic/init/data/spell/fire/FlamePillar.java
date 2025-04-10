package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.IgniteProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class FlamePillar extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> FLAME_PILLAR = spell("flame_pillar");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(FLAME_PILLAR.location()), "Flame Pillar");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				flamePillar(new DataGenContext(ctx)),
				Items.LAVA_BUCKET, 3500,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, FLAME_PILLAR);
	}

	private static ConfiguredEngine<?> flamePillar(DataGenContext ctx) {
		return new ListLogic(List.of(
				new RingRandomIterator(
						DoubleVariable.of("0"),
						DoubleVariable.of("3"),
						DoubleVariable.of("0"),
						DoubleVariable.of("360"),
						IntVariable.of("200"),
						new DustParticleInstance(
								ColorVariable.Static.of(0xFF7F00),
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								IntVariable.of("30")
						),
						null
				),
				new DelayLogic(
						IntVariable.of("20"),
						new ListLogic(List.of(
								new MoveEngine(
										List.of(
												new SetDirectionModifier(
														DoubleVariable.ZERO,
														DoubleVariable.of("1"),
														DoubleVariable.ZERO
												)
										),
										new ProcessorEngine(
												SelectionType.ENEMY,
												new ApproxCylinderSelector(
														DoubleVariable.of("5"),
														DoubleVariable.of("4")
												),
												List.of(
														new DamageProcessor(ctx.damage(DamageTypes.LAVA),
																DoubleVariable.of("10"), true, true),
														new IgniteProcessor(
																List.of(),
																IntVariable.of("200")
														)
												)
										)
								),
								new RingRandomIterator(
										DoubleVariable.of("0"),
										DoubleVariable.of("3"),
										DoubleVariable.of("0"),
										DoubleVariable.of("360"),
										IntVariable.of("200"),
										new RandomVariableLogic(
												"r",
												2,
												new MoveEngine(
														List.of(
																new SetDirectionModifier(
																		DoubleVariable.ZERO,
																		DoubleVariable.of("1"),
																		DoubleVariable.ZERO
																),
																new RotationModifier(
																		DoubleVariable.ZERO,
																		DoubleVariable.of("10*r0-5")
																)
														),
														new DustParticleInstance(
																ColorVariable.Static.of(0xFF7F00),
																DoubleVariable.of("1"),
																DoubleVariable.of("0.2+r1"),
																IntVariable.of("40")
														)
												)
										),
										null
								)
						))

				)
		));
	}

}
