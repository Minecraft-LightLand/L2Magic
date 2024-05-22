package dev.xkmc.l2magic.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2magic.content.engine.core.*;
import dev.xkmc.l2magic.content.engine.helper.EngineRegistryInstance;
import dev.xkmc.l2magic.content.engine.instance.damage.DamageInstance;
import dev.xkmc.l2magic.content.engine.instance.logic.*;
import dev.xkmc.l2magic.content.engine.instance.particle.*;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.selector.*;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class EngineRegistry {

	public static final ResourceKey<Registry<SpellAction>> SPELL = ResourceKey.createRegistryKey(L2Magic.loc("spell_action"));

	public static final EngineRegistryInstance<EngineType<?>> ENGINE = EngineRegistryInstance.of("configured_engine");
	public static final EngineRegistryInstance<ModifierType<?>> MODIFIER = EngineRegistryInstance.of("modifier");
	public static final EngineRegistryInstance<SelectorType<?>> SELECTOR = EngineRegistryInstance.of("selector");

	public static final RegistryEntry<ModifierType<ForwardOffsetModifier>> FORWARD = register("forward", () -> ForwardOffsetModifier.CODEC);
	public static final RegistryEntry<ModifierType<RotationModifier>> ROTATE = register("rotate", () -> RotationModifier.CODEC);
	public static final RegistryEntry<ModifierType<OffsetModifier>> OFFSET = register("offset", () -> OffsetModifier.CODEC);
	public static final RegistryEntry<ModifierType<SetDirectionModifier>> DIRECTION = register("direction", () -> SetDirectionModifier.CODEC);
	public static final RegistryEntry<ModifierType<RandomOffsetModifier>> RANDOM_OFFSET = register("random_offset", () -> RandomOffsetModifier.CODEC);
	public static final RegistryEntry<ModifierType<SetNormalModifier>> NORMAL = register("set_normal", () -> SetNormalModifier.CODEC);
	public static final RegistryEntry<ModifierType<Dir2NormalModifier>> DIR_2_NORMAL = register("direction_to_normal", () -> Dir2NormalModifier.CODEC);
	public static final RegistryEntry<ModifierType<Normal2DirModifier>> NORMAL_2_DIR = register("normal_to_direction", () -> Normal2DirModifier.CODEC);

	public static final RegistryEntry<EngineType<PredicateLogic>> IF = register("if", () -> PredicateLogic.CODEC);
	public static final RegistryEntry<EngineType<ListLogic>> LIST = register("list", () -> ListLogic.CODEC);
	public static final RegistryEntry<EngineType<DelayLogic>> DELAY = register("delay", () -> DelayLogic.CODEC);
	public static final RegistryEntry<EngineType<RandomVariableLogic>> RANDOM = register("random", () -> RandomVariableLogic.CODEC);
	public static final RegistryEntry<EngineType<MoveEngine>> MOVE_ENGINE = register("move", () -> MoveEngine.CODEC);

	public static final RegistryEntry<EngineType<LoopIterator>> ITERATE = register("iterate", () -> LoopIterator.CODEC);
	public static final RegistryEntry<EngineType<DelayedIterator>> ITERATE_DELAY = register("iterate_delayed", () -> DelayedIterator.CODEC);
	public static final RegistryEntry<EngineType<LinearIterator>> ITERATE_LINEAR = register("iterate_linear", () -> LinearIterator.CODEC);
	public static final RegistryEntry<EngineType<RingIterator>> ITERATE_ARC = register("iterate_arc", () -> RingIterator.CODEC);
	public static final RegistryEntry<EngineType<RingRandomIterator>> RANDOM_FAN = register("random_pos_fan", () -> RingRandomIterator.CODEC);

	public static final RegistryEntry<EngineType<DamageInstance>> DAMAGE = register("damage", () -> DamageInstance.CODEC);
	public static final RegistryEntry<EngineType<SimpleParticleInstance>> SIMPLE_PARTICLE = register("particle", () -> SimpleParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<BlockParticleInstance>> BLOCK_PARTICLE = register("block_particle", () -> BlockParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<ItemParticleInstance>> ITEM_PARTICLE = register("item_particle", () -> ItemParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<DustParticleInstance>> DUST_PARTICLE = register("dust_particle", () -> DustParticleInstance.CODEC);
	public static final RegistryEntry<EngineType<TransitionParticleInstance>> TRANSITION_PARTICLE = register("transition_particle", () -> TransitionParticleInstance.CODEC);

	public static final RegistryEntry<SelectorType<MoveSelector>> MOVE_SELECTOR = register("move", () -> MoveSelector.CODEC);
	public static final RegistryEntry<SelectorType<BoxSelector>> BOX = register("box", () -> BoxSelector.CODEC);
	public static final RegistryEntry<SelectorType<CompoundEntitySelector>> COMPOUND = register("compound", () -> CompoundEntitySelector.CODEC);
	public static final RegistryEntry<SelectorType<LinearCubeSelector>> LINEAR = register("line", () -> LinearCubeSelector.CODEC);
	public static final RegistryEntry<SelectorType<ArcCubeSelector>> ARC = register("arc", () -> ArcCubeSelector.CODEC);
	public static final RegistryEntry<SelectorType<ApproxCylinderSelector>> CYLINDER = register("cylinder", () -> ApproxCylinderSelector.CODEC);;
	public static final RegistryEntry<SelectorType<ApproxBallSelector>> BALL = register("ball", () -> ApproxBallSelector.CODEC);

	private static <T extends Record & ConfiguredEngine<T>> RegistryEntry<EngineType<T>>
	register(String id, EngineType<T> codec) {
		return L2Magic.REGISTRATE.simple(id, ENGINE.key(), () -> codec);
	}

	private static <T extends Record & EntitySelector<T>> RegistryEntry<SelectorType<T>>
	register(String id, SelectorType<T> codec) {
		return L2Magic.REGISTRATE.simple(id, SELECTOR.key(), () -> codec);
	}

	private static <T extends Record & Modifier<T>> RegistryEntry<ModifierType<T>>
	register(String id, ModifierType<T> codec) {
		return L2Magic.REGISTRATE.simple(id, MODIFIER.key(), () -> codec);
	}

	public static void register() {

	}

}
