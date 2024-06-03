package dev.xkmc.l2magic.init.data.spell.ice;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ArcCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
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
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;

import java.util.List;

public class WinterStorm extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> WINTER = spell("winter_storm");
	public static final ResourceKey<SpellAction> TORNADO = spell("tornado");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(WINTER.location()), "Winter Storm");
		pvd.add(SpellAction.lang(TORNADO.location()), "Tornado");
	}

	@Override
	public void register(BootstapContext<SpellAction> ctx) {
		new SpellAction(
				winterStorm(new DataGenContext(ctx), 4, 1.5, 1),
				Items.SNOWBALL, 100,
				SpellCastType.CONTINUOUS,
				SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, WINTER);
		new SpellAction(
				tornado(new DataGenContext(ctx)),
				Items.POWDER_SNOW_BUCKET, 100,
				SpellCastType.CONTINUOUS,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, TORNADO);
	}

	private static ConfiguredEngine<?> winterStorm(DataGenContext ctx, double r, double y, double size) {
		return new ListLogic(List.of(
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10"),
						new ProcessorEngine(SelectionType.ENEMY,
								new ArcCubeSelector(
										IntVariable.of("11"),
										DoubleVariable.of(r + ""),
										DoubleVariable.of(size * 2 + ""),
										DoubleVariable.of("-180"),
										DoubleVariable.of("-180+360/12*11")
								),
								List.of(
										new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
												DoubleVariable.of("4"), true, true),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.of("75"),
												DoubleVariable.ZERO,
												PushProcessor.Type.TO_CENTER
										),
										new EffectProcessor(
												LCEffects.ICE.get(),
												IntVariable.of("100"),
												IntVariable.of("0"),
												false, false
										)
								)), null),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new RingRandomIterator(
								DoubleVariable.of((r - size) + ""),
								DoubleVariable.of((r + size) + ""),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("5"),
								new MoveEngine(List.of(
										RotationModifier.of("75"),
										OffsetModifier.of("0", "rand(" + (y - size) + "," + (y + size) + ")", "0")),
										new SimpleParticleInstance(
												ParticleTypes.SNOWFLAKE,
												DoubleVariable.of("0.5")
										)
								), null
						), null
				)
		));
	}


	private static ConfiguredEngine<?> tornado(DataGenContext ctx) {
		double rate = Math.tan(15 * Mth.DEG_TO_RAD);
		return new ListLogic(List.of(
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10"),
						new ProcessorEngine(SelectionType.ENEMY,
								new LinearCubeSelector(
										IntVariable.of("5"),
										DoubleVariable.of("1")
								),
								List.of(
										new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
												DoubleVariable.of("4"), true, true),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.TO_CENTER
										),
										new EffectProcessor(
												LCEffects.ICE.get(),
												IntVariable.of("100"),
												IntVariable.of("0"),
												false, false
										)
								)), null),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new RandomVariableLogic("r", 1,
								new MoveEngine(List.of(
										ForwardOffsetModifier.of("r0*2"),
										new Dir2NormalModifier()
								), new RingRandomIterator(
										DoubleVariable.of("r0*2*" + rate),
										DoubleVariable.of("r0*2*" + rate),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("5"),
										new MoveEngine(List.of(RotationModifier.of("75", "75")),
												new SimpleParticleInstance(
														ParticleTypes.SNOWFLAKE,
														DoubleVariable.of("0.5")
												)
										), null
								))
						), null
				)
		));
	}

}
