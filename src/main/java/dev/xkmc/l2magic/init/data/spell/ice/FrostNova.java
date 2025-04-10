package dev.xkmc.l2magic.init.data.spell.ice;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class FrostNova extends SpellDataGenEntry {
	public static final ResourceKey<SpellAction> FROST_NOVA = spell("frost_nova");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(FROST_NOVA.location()), "Frost Nova");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				frostNova(new DataGenContext(ctx)),
				Items.SNOWBALL.asItem(), 3100,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, FROST_NOVA);
	}

	private static ConfiguredEngine<?> frostNova(DataGenContext ctx) {
		return new ListLogic(List.of(
				new MoveEngine(
						List.of(
								new SetPosModifier(
										DoubleVariable.of("PosX"),
										DoubleVariable.of("PosY+1"),
										DoubleVariable.of("PosZ")
								)
						),
						new ListLogic(List.of(
								new BlockParticleInstance(  // Render
										Blocks.ICE,
										DoubleVariable.ZERO,
										DoubleVariable.of("4"),
										IntVariable.of("20"),
										false
								),
								new RingRandomIterator(  // Render
										DoubleVariable.ZERO,
										DoubleVariable.of(".5"),
										DoubleVariable.ZERO,
										DoubleVariable.of("360"),
										IntVariable.of("100"),
										new RandomVariableLogic(
												"v",
												1,
												new DustParticleInstance(
														ColorVariable.Static.of(0x00FFFF),
														DoubleVariable.of("1"),
														DoubleVariable.of("0.5+0.5*v0"),
														IntVariable.of("40")
												)
										),
										null
								)
						))
				),
				new ProcessorEngine(  // Damage
						SelectionType.ENEMY,
						new ApproxCylinderSelector(
								DoubleVariable.of("8"),
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
				)
		));
	}
}
