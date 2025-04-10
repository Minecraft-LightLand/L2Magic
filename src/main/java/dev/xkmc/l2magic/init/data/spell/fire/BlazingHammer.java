package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.OrPredicate;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.IgniteProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.DustParticleData;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class BlazingHammer extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> BLAZING_HAMMER = spell("blazing_hammer");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(BLAZING_HAMMER.location()), "Blazing Hammer");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				blazingHammer(new DataGenContext(ctx)),
				Items.LAVA_BUCKET, 4000,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, BLAZING_HAMMER);
	}

	private static ConfiguredEngine<?> blazingHammer(DataGenContext ctx) {
		return new ListLogic(List.of(
				new LoopIterator(
						IntVariable.of("100"),
						new MoveEngine(
								List.of(
										new RandomOffsetModifier(
												RandomOffsetModifier.Type.RECT,
												DoubleVariable.of("1"),
												DoubleVariable.ZERO,
												DoubleVariable.of("1")
										)
								),
								new DustParticleInstance(
										ColorVariable.Static.of(0x000000),
										DoubleVariable.of("1"),
										DoubleVariable.ZERO,
										IntVariable.of("55")
								)
						),
						null
				),
				new RandomVariableLogic(
						"r",
						3,
						new MoveEngine(
								List.of(
										new SetPosModifier(
												DoubleVariable.of("PosX"),
												DoubleVariable.of("PosY+2"),
												DoubleVariable.of("PosZ")
										),
										new SetDirectionModifier(
												DoubleVariable.of("r0-min(r0,r1)"),
												DoubleVariable.ZERO,
												DoubleVariable.of("r1-min(r0,r1)")
										),
										new ForwardOffsetModifier(
												DoubleVariable.of("5*max(-1,min(1,r2*100000-50000))")
										)
								),
								new ListLogic(List.of(
										new LoopIterator(
												IntVariable.of("20"),
												new CustomParticleInstance(
														DoubleVariable.ZERO,
														DoubleVariable.of("0.1"),
														IntVariable.of("40"),
														false,
														new MovePosMotion(List.of(
																new SetPosModifier(
																		DoubleVariable.of("PosX+(r0-min(r0,r1))/(r1-r0)*max(-1,min(1,r2*100000-50000))*0.2*l*sin(min(TickCount*TickCount,400)*pi/2/400)"),
																		DoubleVariable.of("PosY+0.2*l*cos(min(TickCount*TickCount, 400)*pi/2/400)"),
																		DoubleVariable.of("PosZ+(r1-min(r0,r1))/(r0-r1)*max(-1,min(1,r2*100000-50000))*0.2*l*sin(min(TickCount*TickCount,400)*pi/2/400)")
																)
														)),
														new DustParticleData(
																RenderTypePreset.NORMAL,
																ColorVariable.Static.of(0x000000)
														)
												),
												"l"
										),
										new LoopIterator(
												IntVariable.of("(2+2*(r0-min(r0,r1))/(r0-r1))/0.2+1"),
												new LoopIterator(
														IntVariable.of("11"),
														new LoopIterator(
																IntVariable.of("(2+2*(r1-min(r0,r1))/(r1-r0))/0.2+1"),
																new PredicateLogic(
																		new OrPredicate(List.of(
																				BooleanVariable.of("xInd*yInd*zInd==0"),
																				BooleanVariable.of("xInd>=(2+2*(r0-min(r0,r1))/(r0-r1))/0.2"),
																				BooleanVariable.of("yInd>=10"),
																				BooleanVariable.of("zInd>=(2+2*(r1-min(r0,r1))/(r1-r0))/0.2")
																		)),
																		new CustomParticleInstance(
																				DoubleVariable.ZERO,
																				DoubleVariable.of("0.1"),
																				IntVariable.of("40"),
																				false,
																				new MovePosMotion(List.of(
																						new SetPosModifier(
																								DoubleVariable.of("PosX+(r0-min(r0,r1))/(r0-r1)*((0.2*xInd-2)*cos(min(TickCount*TickCount,400)*pi/2/400)-max(-1,min(1,r2*100000-50000))*(0.2*yInd+4)*sin(min(TickCount*TickCount,400)*pi/2/400))+(r1-min(r0,r1))/(r1-r0)*(0.2*xInd-1)"),
																								DoubleVariable.of("PosY+(0.2*yInd+4)*cos(min(TickCount*TickCount, 400)*pi/2/400)+max(-1,min(1,r2*100000-50000))*(r0-min(r0,r1))/(r0-r1)*(0.2*xInd-2)*sin(min(TickCount*TickCount,400)*pi/2/400)+max(-1,min(1,r2*100000-50000))*(r1-min(r0,r1))/(r1-r0)*(0.2*zInd-2)*sin(min(TickCount*TickCount,400)*pi/2/400)"),
																								DoubleVariable.of("PosZ+(r1-min(r0,r1))/(r1-r0)*((0.2*zInd-2)*cos(min(TickCount*TickCount,400)*pi/2/400)-max(-1,min(1,r2*100000-50000))*(0.2*yInd+4)*sin(min(TickCount*TickCount,400)*pi/2/400))+(r0-min(r0,r1))/(r0-r1)*(0.2*zInd-1)")
																						)
																				)),
																				new DustParticleData(
																						RenderTypePreset.NORMAL,
																						ColorVariable.Static.of(0x7F7F7F)
																				)
																		),
																		null
																),
																"zInd"
														),
														"yInd"
												),
												"xInd"
										)
								))
						)
				),
				new DelayLogic(
						IntVariable.of("20"),
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
														DoubleVariable.of("10"), true, true),
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
										SoundEvents.ANVIL_LAND,
										DoubleVariable.of("5"),
										DoubleVariable.ZERO
								)
						))
				)
		));
	}

}
