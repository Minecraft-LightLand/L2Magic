package dev.xkmc.l2magic.init.data;

import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.SpellAction;
import dev.xkmc.l2magic.content.engine.core.SpellCastType;
import dev.xkmc.l2magic.content.engine.core.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.instance.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LMDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	public static final ResourceKey<SpellAction> WINTER = spell("winter_storm");
	public static final ResourceKey<SpellAction> FLAME = spell("flame_burst");

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(EngineRegistry.SPELL, ctx -> {
				ctx.register(WINTER, new SpellAction(
						winterStorm(),
						Items.SNOWBALL,
						SpellCastType.CONTINUOUS,
						SpellTriggerType.SELF_POS
				));
				ctx.register(FLAME, new SpellAction(
						flameBurst(),
						Items.FIRE_CHARGE,
						SpellCastType.INSTANT,
						SpellTriggerType.TARGET_POS
				));

			});

	private static ConfiguredEngine<?> winterStorm() {
		return new DelayedIterator(
				IntVariable.of("5"),
				IntVariable.of("4"),
				new RingRandomIterator(
						DoubleVariable.of("3"),
						DoubleVariable.of("8"),
						DoubleVariable.of("-180"),
						DoubleVariable.of("180"),
						IntVariable.of("3"),
						new RotationModifier(
								DoubleVariable.of("75"),
								new RandomOffsetModifier(
										RandomOffsetModifier.Type.RECT,
										DoubleVariable.ZERO,
										DoubleVariable.of("1.5"),
										DoubleVariable.ZERO,
										new SimpleParticleInstance(
												ParticleTypes.SNOWFLAKE,
												DoubleVariable.of("0.5")
										)
								)
						), null
				), null
		);
	}

	private static ConfiguredEngine<?> flameBurst() {
		return new DelayedIterator(
				IntVariable.of("40"),
				IntVariable.of("1"),
				new RingRandomIterator(
						DoubleVariable.of("0"),
						DoubleVariable.of("4"),
						DoubleVariable.of("-180"),
						DoubleVariable.of("180"),
						IntVariable.of("10"),
						new SetDirectionModifier(
								DoubleVariable.ZERO,
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								new SimpleParticleInstance(
										ParticleTypes.FLAME,
										DoubleVariable.of("1.5")
								)
						), null
				), null
		);
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
