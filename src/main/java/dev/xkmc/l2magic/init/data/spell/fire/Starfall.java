package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.IgniteProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class Starfall extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> STARFALL = spell("starfall");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(STARFALL.location()), "Starfall");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				starfall(new DataGenContext(ctx)),
				Items.BASALT, 3900,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, STARFALL);
	}

	private static ConfiguredEngine<?> starfall(DataGenContext ctx) {
		return new ListLogic(List.of(
				new RingRandomIterator(
						DoubleVariable.ZERO,
						DoubleVariable.of("1.5"),
						DoubleVariable.ZERO,
						DoubleVariable.of("360"),
						IntVariable.of("100"),
						new DustParticleInstance(
								ColorVariable.Static.of(0x000000),
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								IntVariable.of("55")
						),
						null
				),
				new MoveEngine(
						List.of(
								new SetDirectionModifier(
										DoubleVariable.ZERO,
										DoubleVariable.of("-1"),
										DoubleVariable.ZERO
								),
								new ForwardOffsetModifier(
										DoubleVariable.of("-4")
								)
						),
						new ListLogic(List.of(
								new SphereRandomIterator(
										DoubleVariable.of("1"),
										IntVariable.of("1000"),
										new MoveEngine(
												List.of(
														new RandomOffsetModifier(
																RandomOffsetModifier.Type.RECT,
																DoubleVariable.of("0.2"),
																DoubleVariable.of("0.2"),
																DoubleVariable.of("0.2")
														),
														new SetDirectionModifier(
																DoubleVariable.ZERO,
																DoubleVariable.of("-1"),
																DoubleVariable.ZERO
														)
												),
												new DustParticleInstance(
														ColorVariable.Static.of(0x000000),
														DoubleVariable.of("1"),
														DoubleVariable.of("0.1"),
														IntVariable.of("50")
												)
										),
										null
								),
								new DelayedIterator(
										IntVariable.of("40"),
										IntVariable.of("1"),
										new SphereRandomIterator(
												DoubleVariable.of("1.5"),
												IntVariable.of("50"),
												new MoveEngine(
														List.of(
																new SetDirectionModifier(
																		DoubleVariable.ZERO,
																		DoubleVariable.of("-1"),
																		DoubleVariable.ZERO
																),
																new ForwardOffsetModifier(
																		DoubleVariable.of("t*0.1")
																)
														),
														new DustParticleInstance(
																ColorVariable.Static.of(0xFF0000),
																DoubleVariable.of("0.5"),
																DoubleVariable.of("-0.2"),
																IntVariable.of("5")
														)
												),
												null
										),
										"t"
								)
						))
				),
				new DelayLogic(
						IntVariable.of("45"),
						new ListLogic(List.of(
								new RingRandomIterator(
										DoubleVariable.of("0"),
										DoubleVariable.of("2"),
										DoubleVariable.of("0"),
										DoubleVariable.of("360"),
										IntVariable.of("200"),
										new RandomVariableLogic(
												"r",
												2,
												new MoveEngine(
														List.of(
																new RotationModifier(
																		DoubleVariable.ZERO,
																		DoubleVariable.of("45*r0")
																)
														),
														new DustParticleInstance(
																ColorVariable.Static.of(0xFF0000),
																DoubleVariable.of("1"),
																DoubleVariable.of("0.2+r1"),
																IntVariable.of("40")
														)
												)
										),
										null
								),
								new ProcessorEngine(
										SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("8"),
												DoubleVariable.of("4")
										),
										List.of(
												new DamageProcessor(ctx.damage(DamageTypes.IN_FIRE),
														DoubleVariable.of("20"), true, true),
												new IgniteProcessor(
														List.of(),
														IntVariable.of("200")
												),
												new KnockBackProcessor(
														DoubleVariable.of("2"),
														DoubleVariable.of("45"),
														DoubleVariable.ZERO
												)
										)
								),
								new SoundInstance(
										SoundEvents.GENERIC_EXPLODE.value(),
										DoubleVariable.of("5"),
										DoubleVariable.ZERO
								)
						))
				)
		));
	}

}
