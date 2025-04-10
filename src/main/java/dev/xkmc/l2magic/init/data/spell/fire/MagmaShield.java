package dev.xkmc.l2magic.init.data.spell.fire;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.IgniteProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.selector.SelfSelector;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

import java.util.List;

public class MagmaShield extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> MAGMA_SHIELD = spell("magma_shield");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(MAGMA_SHIELD.location()), "Magma Shield");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				magmaShield(new DataGenContext(ctx)),
				Items.MAGMA_BLOCK, 3400,
				SpellCastType.INSTANT,
				SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, MAGMA_SHIELD);
	}

	private static ConfiguredEngine<?> magmaShield(DataGenContext ctx) {
		return new ListLogic(List.of(
				new DelayedIterator(  // Protection
						IntVariable.of("60"),
						IntVariable.of("10"),
						new ProcessorEngine(
								SelectionType.ALL,
								new SelfSelector(),
								List.of(new EffectProcessor(
										MobEffects.DAMAGE_RESISTANCE,
										IntVariable.of("39"),
										IntVariable.of("0"),
										false, false
								))
						),
						null
				),
				new DelayedIterator(
						IntVariable.of("600"),
						IntVariable.of("1"),
						new MoveEngine(
								List.of(
										new SetPosModifier(
												DoubleVariable.of("CasterX"),
												DoubleVariable.of("CasterY+0.95"),
												DoubleVariable.of("CasterZ")
										)
								),
								new ListLogic(List.of(
										new PredicateLogic(  // Damage
												BooleanVariable.of("t%10==0"),
												new ProcessorEngine(
														SelectionType.ENEMY,
														new ApproxCylinderSelector(
																DoubleVariable.of("2"),
																DoubleVariable.of("1")
														),
														List.of(
																new DamageProcessor(ctx.damage(DamageTypes.HOT_FLOOR),
																		DoubleVariable.of("2"), true, true),
																new KnockBackProcessor(
																		DoubleVariable.of("0.1"),
																		DoubleVariable.ZERO,
																		DoubleVariable.ZERO
																),
																new IgniteProcessor(
																		List.of(),
																		IntVariable.of("40")
																)
														)
												),
												null
										),
										new SphereRandomIterator(
												DoubleVariable.of("1.5"),
												IntVariable.of("50"),
												new DustParticleInstance(
														ColorVariable.Static.of(0xFF7F00),
														DoubleVariable.of("1"),
														DoubleVariable.ZERO,
														IntVariable.of("3")
												),
												null
										)
								))
						),
						"t"
				)
		));
	}

}
