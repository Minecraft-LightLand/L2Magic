package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ArrowSpells extends SpellDataGenEntry {


	public static final ResourceKey<SpellAction> ARROW = spell("magic_arrows");
	public static final ResourceKey<SpellAction> ARROW_RING = spell("magic_arrow_ring");
	public static final ResourceKey<SpellAction> CIRCULAR = spell("circular");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(ARROW_RING.location()), "Sword of Seven");
		pvd.add(SpellAction.lang(ARROW.location()), "Angelic Judgement");
		pvd.add(SpellAction.lang(CIRCULAR.location()), "Three Bodies");
	}

	@Override
	public void register(BootstapContext<SpellAction> ctx) {
		new SpellAction(
				arrowRing(new DataGenContext(ctx)),
				Items.SPECTRAL_ARROW, 400,
				SpellCastType.INSTANT,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, ARROW_RING);
		new SpellAction(
				arrows(new DataGenContext(ctx)),
				Items.ARROW, 500,
				SpellCastType.INSTANT,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, ARROW);
		new SpellAction(
				circular(new DataGenContext(ctx)),
				Items.CONDUIT, 600,
				SpellCastType.INSTANT,
				SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, CIRCULAR);
	}

	private static ConfiguredEngine<?> arrowRing(DataGenContext ctx) {
		return new ListLogic(List.of(
				new MoveEngine(List.of(OffsetModifier.of("0", "-0.1", "0")),
						shoot(ctx)),
				new RandomVariableLogic("r", 1,
						new MoveEngine(List.of(new Dir2NormalModifier()),
								new RingIterator(
										DoubleVariable.of("0.5"),
										DoubleVariable.of("r0*360"),
										DoubleVariable.of("360+r0*360"),
										IntVariable.of("7"),
										false,
										new MoveEngine(List.of(RotationModifier.of("0", "75")),
												shoot(ctx)),
										null
								)))
		));
	}

	private static ConfiguredEngine<?> arrows(DataGenContext ctx) {
		return new MoveEngine(List.of(OffsetModifier.of("0", "-0.1", "0")),
				new RingIterator(
						DoubleVariable.of("0.5"),
						DoubleVariable.of("-30"),
						DoubleVariable.of("30"),
						IntVariable.of("7"),
						true,
						shootMove(ctx),
						null
				));
	}

	private static ConfiguredEngine<?> circular(DataGenContext ctx) {
		return new DelayedIterator(
				IntVariable.of("60"),
				IntVariable.of("1"),
				new MoveEngine(List.of(
						new ToCurrentCasterPosModifier(),
						OffsetModifier.of("0", "1", "0")),
						new RingIterator(
								DoubleVariable.of("i*0.1+0.5"),
								DoubleVariable.of("-180+i*14"),
								DoubleVariable.of("180+i*14"),
								IntVariable.of("3"),
								false,
								new ListLogic(List.of(
										new ProcessorEngine(SelectionType.ENEMY,
												new BoxSelector(
														DoubleVariable.of("1"),
														DoubleVariable.of("1"),
														true
												), List.of(
												new DamageProcessor(
														ctx.damage(DamageTypes.INDIRECT_MAGIC),
														DoubleVariable.of("6"),
														true, true),
												new PushProcessor(
														DoubleVariable.of("0.3"),
														DoubleVariable.of("0"),
														DoubleVariable.of("10"),
														PushProcessor.Type.UNIFORM
												)
										)),
										new SimpleParticleInstance(
												ParticleTypes.END_ROD,
												DoubleVariable.ZERO
										)
								)),
								null
						)
				), "i"
		);
	}

	private static ConfiguredEngine<?> shoot(DataGenContext ctx) {
		int dis = 24;
		double step = 0.2;
		double rad = 1;
		return new ListLogic(List.of(
				new ProcessorEngine(SelectionType.ENEMY,
						new LinearCubeSelector(
								IntVariable.of(dis / rad + ""),
								DoubleVariable.of(rad + "")
						), List.of(new DamageProcessor(
								ctx.damage(DamageTypes.INDIRECT_MAGIC),
								DoubleVariable.of("6"),
								true, true),
						KnockBackProcessor.of("1")
				)),
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

	private static ConfiguredEngine<?> shootMove(DataGenContext ctx) {
		int dis = 24;
		double rad = 1;
		return new DelayedIterator(IntVariable.of(dis + ""), IntVariable.of("1"),
				new MoveEngine(
						List.of(ForwardOffsetModifier.of(rad + "*i")),
						new ListLogic(List.of(
								new ProcessorEngine(SelectionType.ENEMY,
										new BoxSelector(
												DoubleVariable.of(rad + ""),
												DoubleVariable.of(rad + ""),
												true
										), List.of(new DamageProcessor(
												ctx.damage(DamageTypes.INDIRECT_MAGIC),
												DoubleVariable.of("6"),
												true, true),
										new PushProcessor(
												DoubleVariable.of("0.5"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.UNIFORM
										)
								)),
								new SimpleParticleInstance(
										ParticleTypes.END_ROD,
										DoubleVariable.ZERO
								)
						))), "i");
	}


}
