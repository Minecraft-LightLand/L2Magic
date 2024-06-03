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
				Blocks.BEACON.asItem(), 2000,
				SpellCastType.CONTINUOUS,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, MASTER_SPARK);
	}

	private static ConfiguredEngine<?> masterSpark(DataGenContext ctx) {
		return new ListLogic(
				List.of(
						new PredicateLogic(  // charge
								BooleanVariable.of("TickUsing<=25"),
								new SphereRandomIterator(
										DoubleVariable.of("3"),
										IntVariable.of("10"),
										new DustParticleInstance(
												ColorVariable.Static.of(0xFFFFFF),
												DoubleVariable.of("1"),
												DoubleVariable.of("-.2"),
												IntVariable.of("10")
										),
										null
								),
								null
						),
						new PredicateLogic(
								BooleanVariable.of("TickUsing>50"),
								new ListLogic(List.of(
										new ProcessorEngine(SelectionType.ENEMY,
												new LinearCubeSelector(
														IntVariable.of("6"), // dist=18, r=3
														DoubleVariable.of("3")
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
														OffsetModifier.of("0", "-3", "0"),
														new Dir2NormalModifier()
												),
												new RingRandomIterator(
														DoubleVariable.ZERO,
														DoubleVariable.of("3"),
														DoubleVariable.of("-180"),
														DoubleVariable.of("180"),
														IntVariable.of("20"),
														new RandomVariableLogic("r", 4,
																new MoveEngine(List.of(RotationModifier.of("0", "105-30*r0")),
																		new DustParticleInstance(
																				ColorVariable.Static.of(0xFFFFFF),
																				DoubleVariable.of("4+4*r1"),
																				DoubleVariable.of("2+2*r2"),
																				IntVariable.of("10+10*r3")
																		)
																)
														),
														null
												)
										))
								),
								null
						)
				)
		);
	}

}
