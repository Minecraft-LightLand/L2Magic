package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.SpellAction;
import dev.xkmc.l2magic.content.engine.core.SpellCastType;
import dev.xkmc.l2magic.content.engine.core.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.instance.damage.DamageInstance;
import dev.xkmc.l2magic.content.engine.instance.logic.*;
import dev.xkmc.l2magic.content.engine.instance.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.instance.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.ArcCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LMDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	public static final ResourceKey<SpellAction> WINTER = spell("winter_storm");
	public static final ResourceKey<SpellAction> FLAME = spell("flame_burst");
	public static final ResourceKey<SpellAction> QUAKE = spell("earthquake");
	public static final ResourceKey<SpellAction> ARROW = spell("magic_arrows");

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(EngineRegistry.SPELL, ctx -> {
				new SpellAction(
						winterStorm(new DataGenContext(ctx)),
						Items.SNOWBALL,
						SpellCastType.CONTINUOUS,
						SpellTriggerType.SELF_POS
				).verifyOnBuild(ctx, WINTER);
				new SpellAction(
						flameBurst(new DataGenContext(ctx)),
						Items.FIRE_CHARGE,
						SpellCastType.INSTANT,
						SpellTriggerType.TARGET_POS
				).verifyOnBuild(ctx, FLAME);
				new SpellAction(
						flameBurstCircle(new DataGenContext(ctx)),
						Items.TNT,
						SpellCastType.CHARGE,
						SpellTriggerType.HORIZONTAL_FACING
				).verifyOnBuild(ctx, QUAKE);
				new SpellAction(
						arrows(new DataGenContext(ctx)),
						Items.ARROW,
						SpellCastType.INSTANT,
						SpellTriggerType.FACING_FRONT
				).verifyOnBuild(ctx, ARROW);
			});

	private static ConfiguredEngine<?> winterStorm(DataGenContext ctx) {
		return new ListLogic(List.of(
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10&TickUsing%4==0"),
						new DamageInstance(
								new ArcCubeSelector(
										IntVariable.of("11"),
										DoubleVariable.of("5.5"),
										DoubleVariable.of("2.5"),
										DoubleVariable.of("-180"),
										DoubleVariable.of("-180+360/12*11")
								), ctx.damage(DamageTypes.FREEZE),
								DoubleVariable.of("4"),
								DoubleVariable.of("1"),
								true, true
						), null),
				new DelayedIterator(
				IntVariable.of("10"),
				IntVariable.of("2"),
						new RingRandomIterator(
								DoubleVariable.of("3"),
								DoubleVariable.of("8"),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("5"),
								new MoveEngine(List.of(
										RotationModifier.of("75"),
										OffsetModifier.of("0", "rand(0.5,2.5)", "0")
								),
										new SimpleParticleInstance(
												ParticleTypes.SNOWFLAKE,
												DoubleVariable.of("0.5")
										)
								), null
						), null
				)
		));
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
								new PredicateLogic(
										BooleanVariable.of("Time%2==0"),
										new DamageInstance(
												new ApproxCylinderSelector(
														DoubleVariable.of("4"),
														DoubleVariable.of("6")
												), ctx.damage(DamageTypes.IN_FIRE),
												DoubleVariable.of("4"),
												DoubleVariable.ZERO,
												true, false
										), null),
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

	private static ConfiguredEngine<?> flameBurstCircle(DataGenContext ctx) {
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
																RandomOffsetModifier.Type.BALL,
																DoubleVariable.of("0.1"),
																DoubleVariable.ZERO,
																DoubleVariable.of("0.1")
														)),
												new ListLogic(List.of(
														new MoveEngine(
																List.of(RotationModifier.of("rand(0,360)")),
																star(2, 0.2)),
														new DamageInstance(
																new ApproxCylinderSelector(
																		DoubleVariable.of("4"),
																		DoubleVariable.of("2")
																), ctx.damage(DamageTypes.EXPLOSION),
																DoubleVariable.of("10"),
																DoubleVariable.of("2"),
																true, true
														),
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
																				DoubleVariable.of("0.5+rand(0,0.4)")
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

	private static ConfiguredEngine<?> arrows(DataGenContext ctx) {
		return new ListLogic(List.of(
				new MoveEngine(List.of(OffsetModifier.of("0", "-0.1", "0"),
						new Normal2DirModifier()),
						shoot(ctx)),
				new RandomVariableLogic("r", 1,
						new RingIterator(
								DoubleVariable.of("0.5"),
								DoubleVariable.of("r0*360"),
								DoubleVariable.of("360+r0*360"),
								IntVariable.of("7"),
								false,
								new MoveEngine(List.of(RotationModifier.of("0", "75")),
										shoot(ctx)),
								null
						))
		));
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

	private static ConfiguredEngine<?> shoot(DataGenContext ctx) {
		int dis = 24;
		double step = 0.2;
		double rad = 1;
		return new ListLogic(List.of(
				new DamageInstance(
						new LinearCubeSelector(
								IntVariable.of(dis / rad + ""),
								DoubleVariable.of(rad + "")
						),
						ctx.damage(DamageTypes.INDIRECT_MAGIC),
						DoubleVariable.of("6"),
						DoubleVariable.of("1"),
						true, true
				),
				new LinearIterator(
						DoubleVariable.of(step + ""),
						Vec3.ZERO,
						DoubleVariable.ZERO,
						IntVariable.of(dis / step + ""),
						true,
						new SimpleParticleInstance(
								ParticleTypes.END_ROD,
								DoubleVariable.ZERO
						),
						null
				)));
	}

	private static ResourceKey<SpellAction> spell(String id) {
		return ResourceKey.create(EngineRegistry.SPELL, L2Magic.loc(id));
	}

	public LMDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", L2Magic.MODID));
	}

	@NotNull
	public String getName() {
		return "L2Magic Spell Data";
	}

}
