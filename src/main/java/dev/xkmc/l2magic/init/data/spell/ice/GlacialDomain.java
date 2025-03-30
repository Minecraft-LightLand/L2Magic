package dev.xkmc.l2magic.init.data.spell.ice;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.BlockInRangeIterator;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.modifier.ToCurrentCasterPosModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.BlockMatchCondition;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class GlacialDomain extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> GLACIAL_DOMAIN = spell("glacial_domain");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(GLACIAL_DOMAIN.location()), "Glacial Domain");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				glacialDomain(new DataGenContext(ctx)),
				Items.SNOWBALL.asItem(), 4100,
				SpellCastType.INSTANT,
				SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, GLACIAL_DOMAIN);
	}

	private static ConfiguredEngine<?> glacialDomain(DataGenContext ctx) {
		return new DelayedIterator(
				IntVariable.of("30"),
				IntVariable.of("10"),
				new MoveEngine(
						List.of(
								new ToCurrentCasterPosModifier()
						),
						new ListLogic(List.of(
								new RingRandomIterator(  // Render
										DoubleVariable.of(".25"),
										DoubleVariable.of(".5"),
										DoubleVariable.ZERO,
										DoubleVariable.of("360"),
										IntVariable.of("100"),
										new RandomVariableLogic(
												"v",
												1,
												new SimpleParticleInstance(
														ParticleTypes.SNOWFLAKE,
														DoubleVariable.of("0.5+0.5*v0")
												)
										),
										null
								),
								new SoundInstance(  // Sound
										SoundEvents.POWDER_SNOW_PLACE,
										DoubleVariable.of("4"),
										DoubleVariable.ZERO
								),
								new ProcessorEngine(  // Damage
										SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("4"),
												DoubleVariable.of("4")
										),
										List.of(
												new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
														DoubleVariable.of("4"), true, true),
												new KnockBackProcessor(
														DoubleVariable.of("0.1"),
														DoubleVariable.ZERO,
														DoubleVariable.ZERO
												),
												new EffectProcessor(
														MobEffects.MOVEMENT_SLOWDOWN,
														IntVariable.of("100"),
														IntVariable.of("0"),
														false, false
												)
										)
								),
								new MoveEngine(
										List.of(
												new SetPosModifier(
														DoubleVariable.of("PosX"),
														DoubleVariable.of("PosY-1"),
														DoubleVariable.of("PosZ")
												)
										),
										new BlockInRangeIterator(
												DoubleVariable.of("4"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												new PredicateLogic(
														BlockMatchCondition.of(Blocks.WATER),
														new SetBlock(
																Blocks.ICE.defaultBlockState()
														),
														null
												),
												null
										)
								)
						))
				),
				null
		);
	}
}