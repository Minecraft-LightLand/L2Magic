package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlameSpells extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> FLAME = spell("flame_burst");
	public static final ResourceKey<SpellAction> QUAKE = spell("earthquake");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(FLAME.location()), "Hell Mark");
		pvd.add(SpellAction.lang(QUAKE.location()), "Lava Burst");
	}

	@Override
	public void register(BootstapContext<SpellAction> ctx) {
		new SpellAction(
				flameBurst(new DataGenContext(ctx)),
				Items.FIRE_CHARGE, 200,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, FLAME);
		new SpellAction(
				earthquake(new DataGenContext(ctx)),
				Items.TNT, 300,
				SpellCastType.CHARGE,
				SpellTriggerType.HORIZONTAL_FACING
		).verifyOnBuild(ctx, QUAKE);
	}

	private static ConfiguredEngine<?> flameBurst(DataGenContext ctx) {
		return new ListLogic(List.of(
				new MoveEngine(List.of(
						new SetDirectionModifier(
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								DoubleVariable.ZERO),
						RotationModifier.of("rand(0,360)")),
						star(4, 0.3)
				),
				new DelayedIterator(
						IntVariable.of("40"),
						IntVariable.of("1"),
						new ListLogic(List.of(
								new ProcessorEngine(SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("4"),
												DoubleVariable.of("6")
										), List.of(
										new DamageProcessor(
												ctx.damage(DamageTypes.IN_FIRE),
												DoubleVariable.of("4"),
												true, false
										),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.UNIFORM
										),
										new PropertyProcessor(
												PropertyProcessor.Type.IGNITE,
												IntVariable.of("100")
										)
								)),
								new RingRandomIterator(
										DoubleVariable.of("0"),
										DoubleVariable.of("4"),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("10"),
										new RandomVariableLogic(
												"r", 4,
												new MoveEngine(List.of(
														new SetDirectionModifier(
																DoubleVariable.of("(r0-0.5)*0.2"),
																DoubleVariable.of("1"),
																DoubleVariable.of("(r1-0.5)*0.2")
														)),
														new PredicateLogic(
																BooleanVariable.of("r2<0.25"),
																new SimpleParticleInstance(
																		ParticleTypes.SOUL,
																		DoubleVariable.of("0.5+r3*0.2")
																),
																new SimpleParticleInstance(
																		ParticleTypes.FLAME,
																		DoubleVariable.of("0.5+r3*0.2")
																)
														))
										), "i"
								))), null
				)
		));
	}

	private static ConfiguredEngine<?> earthquake(DataGenContext ctx) {
		return new PredicateLogic(BooleanVariable.of("Power==0"),
				new RingRandomIterator(
						DoubleVariable.of("0.5"),
						DoubleVariable.of("1"),
						DoubleVariable.of("-180"),
						DoubleVariable.of("180"),
						IntVariable.of("5*min(TickUsing/10,3)"),
						new MoveEngine(List.of(
								RotationModifier.of("135", "rand(-15*min(floor(TickUsing/10),3),0)"),
								ForwardOffsetModifier.of("-4")),
								new SimpleParticleInstance(
										ParticleTypes.SMALL_FLAME,
										DoubleVariable.of("0.3")
								)
						), null
				),
				earthquakeStart(ctx)
		);
	}

	private static ConfiguredEngine<?> earthquakeStart(DataGenContext ctx) {
		return new DelayedIterator(
				IntVariable.of("min(TickUsing/10,3)"),
				IntVariable.of("10"),
				new RandomVariableLogic("r", 2,
						new LoopIterator(
								IntVariable.of("3+i*2"),
								new DelayLogic(
										IntVariable.of("abs(i+1-j)*1"),
										new MoveEngine(List.of(
												RotationModifier.of("180/(3+i*2)*(j+(r0+r1)/2)-90"),
												ForwardOffsetModifier.of("6*i+4"),
												new RandomOffsetModifier(
														RandomOffsetModifier.Type.SPHERE,
														DoubleVariable.of("0.1"),
														DoubleVariable.ZERO,
														DoubleVariable.of("0.1")
												)),
												new ListLogic(List.of(
														new MoveEngine(
																List.of(RotationModifier.of("rand(0,360)")),
																star(2, 0.2)),
														new ProcessorEngine(SelectionType.ENEMY,
																new ApproxCylinderSelector(
																		DoubleVariable.of("4"),
																		DoubleVariable.of("2")
																), List.of(
																new DamageProcessor(ctx.damage(DamageTypes.EXPLOSION),
																		DoubleVariable.of("10"), true, true),
																KnockBackProcessor.of("2")
														)),
														new RingRandomIterator(
																DoubleVariable.of("0"),
																DoubleVariable.of("2"),
																DoubleVariable.of("-180"),
																DoubleVariable.of("180"),
																IntVariable.of("100"),
																new MoveEngine(List.of(
																		new SetDirectionModifier(
																				DoubleVariable.ZERO,
																				DoubleVariable.of("1"),
																				DoubleVariable.ZERO)),
																		new BlockParticleInstance(
																				Blocks.STONE,
																				DoubleVariable.of("0.5+rand(0,0.4)"),
																				DoubleVariable.of("0.5"),
																				IntVariable.of("rand(20,40)"),
																				true
																		)
																), null
														)
												))
										)
								), "j"
						)
				), "i"
		);
	}

	private static ConfiguredEngine<?> star(double radius, double step) {
		int linestep = (int) Math.round(1.9 * radius / step);
		int circlestep = (int) Math.round(radius * Math.PI * 2 / step);
		return new ListLogic(List.of(
				new LoopIterator(
						IntVariable.of("5"),
						new MoveEngine(List.of(
								RotationModifier.of("72*ri"),
								ForwardOffsetModifier.of(radius + ""),
								RotationModifier.of("162")),
								new LinearIterator(
										DoubleVariable.of(radius * 1.9 / linestep + ""),
										Vec3.ZERO,
										DoubleVariable.ZERO,
										IntVariable.of(linestep + 1 + ""),
										true,
										new SimpleParticleInstance(
												ParticleTypes.FLAME,
												DoubleVariable.ZERO
										),
										null
								)
						), "ri"
				),
				new LoopIterator(
						IntVariable.of(circlestep + ""),
						new MoveEngine(List.of(
								RotationModifier.of(360d / circlestep + "*ri"),
								ForwardOffsetModifier.of(radius + "")
						),
								new SimpleParticleInstance(
										ParticleTypes.FLAME,
										DoubleVariable.ZERO
								)
						), "ri"
				)
		)
		);
	}

}
