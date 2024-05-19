package dev.xkmc.l2magic.init.registrate;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.SpellAction;
import dev.xkmc.l2magic.content.engine.instance.logic.ListInstance;
import dev.xkmc.l2magic.content.engine.instance.logic.PredicateInstance;
import dev.xkmc.l2magic.content.engine.instance.particle.*;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class EngineRegistry {

	public static final ResourceKey<Registry<EngineType<?>>> CONFIG
			= L2Magic.REGISTRATE.makeRegistry("configured_engine", () -> new RegistryBuilder<EngineType<?>>().hasTags());

	public static final ResourceKey<Registry<SpellAction>> SPELL
			= ResourceKey.createRegistryKey(L2Magic.loc("spell_action"));

	public static final Supplier<IForgeRegistry<EngineType<?>>> REGISTRY =
			Suppliers.memoize(() -> Wrappers.cast(RegistryManager.ACTIVE.getRegistry(CONFIG)));

	public static final RegistryEntry<EngineType<PredicateInstance>> IF = register("if", () -> PredicateInstance.CODEC);
	public static final RegistryEntry<EngineType<ListInstance>> LIST = register("list", () -> ListInstance.CODEC);

	public static final RegistryEntry<EngineType<DelayModifier>> DELAY = register("delay", () -> DelayModifier.CODEC);
	public static final RegistryEntry<EngineType<ForwardOffsetModifier>> FORWARD = register("forward", () -> ForwardOffsetModifier.CODEC);
	public static final RegistryEntry<EngineType<RotationModifier>> ROTATE = register("rotate", () -> RotationModifier.CODEC);
	public static final RegistryEntry<EngineType<OffsetModifier>> OFFSET = register("offset", () -> OffsetModifier.CODEC);
	public static final RegistryEntry<EngineType<SetDirectionModifier>> DIRECTION = register("direction", () -> SetDirectionModifier.CODEC);
	public static final RegistryEntry<EngineType<RandomOffsetModifier>> RANDOM_OFFSET = register("random_offset", () -> RandomOffsetModifier.CODEC);

	public static final RegistryEntry<EngineType<LoopIterator>> ITERATE = register("iterate", () -> LoopIterator.CODEC);
	public static final RegistryEntry<EngineType<DelayedIterator>> ITERATE_DELAY = register("iterate_delayed", () -> DelayedIterator.CODEC);
	public static final RegistryEntry<EngineType<LinearIterator>> ITERATE_LINEAR = register("iterate_linear", () -> LinearIterator.CODEC);
	public static final RegistryEntry<EngineType<RingRandomIterator>> RANDOM_FAN = register("random_pos_fan", () -> RingRandomIterator.CODEC);

	public static final RegistryEntry<EngineType<SimpleParticleInstance>> SIMPLE_PARTICLE = register("particle", () -> SimpleParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<BlockParticleInstance>> BLOCK_PARTICLE = register("block_particle", () -> BlockParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<ItemParticleInstance>> ITEM_PARTICLE = register("item_particle", () -> ItemParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<DustParticleInstance>> DUST_PARTICLE = register("dust_particle", () -> DustParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<TransitionParticleInstance>> TRANSITION_PARTICLE = register("transition_particle", () -> TransitionParticleInstance.CODEC);

	private static <T extends Record & ConfiguredEngine<T>> RegistryEntry<EngineType<T>>
	register(String id, EngineType<T> codec) {
		return L2Magic.REGISTRATE.simple(id, CONFIG, () -> codec);
	}

	public static void register() {

	}

}
