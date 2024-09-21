package dev.xkmc.l2magic.init.data.spell.ice;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.filter.MobEffectFilter;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.FilteredProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import dev.xkmc.l2magic.init.data.spell.UnrealHelper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class AbsoluteZero extends SpellDataGenEntry {
	public static final ResourceKey<SpellAction> ABSOLUTE_ZERO = spell("absolute_zero");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(ABSOLUTE_ZERO.location()), "Absolute Zero");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				absoluteZero(new DataGenContext(ctx)),
				Items.BLUE_ICE.asItem(), 3800,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, ABSOLUTE_ZERO);
	}

	private static ConfiguredEngine<?> absoluteZero(DataGenContext ctx) {
		return new ListLogic(List.of(
				new BlockParticleInstance(  // Render
						Blocks.BLUE_ICE,
						DoubleVariable.ZERO,
						DoubleVariable.of("4"),
						IntVariable.of("20"),
						false
				),
				UnrealHelper.cuboidSurface(
						4, 4, 4, 0.2,
						new DustParticleInstance(
								ColorVariable.Static.of(0x00FFFF),
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								IntVariable.of("40")
						)
				),
				new ProcessorEngine(  // Damage
						SelectionType.ENEMY,
						new BoxSelector(
								DoubleVariable.of("4"),
								DoubleVariable.of("4"),
								false
						),
						List.of(
								new FilteredProcessor(
										new MobEffectFilter(MobEffects.MOVEMENT_SLOWDOWN),
										List.of(
												new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
														DoubleVariable.of("10"), true, true),
												new PropertyProcessor(
														PropertyProcessor.Type.FREEZE,
														IntVariable.of("100")
												)
										),
										List.of(
												new DamageProcessor(ctx.damage(DamageTypes.FREEZE),
														DoubleVariable.of("4"), true, true),
												new EffectProcessor(
														MobEffects.MOVEMENT_SLOWDOWN,
														IntVariable.of("100"),
														IntVariable.of("0"),
														false, false
												)
										)
								)
						)
				)
		));
	}
}
