package dev.xkmc.l2magic.init.registrate;

import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.engine.block.*;
import dev.xkmc.l2magic.content.engine.core.*;
import dev.xkmc.l2magic.content.engine.filter.AndFilter;
import dev.xkmc.l2magic.content.engine.filter.MobEffectFilter;
import dev.xkmc.l2magic.content.engine.filter.NotFilter;
import dev.xkmc.l2magic.content.engine.filter.OrFilter;
import dev.xkmc.l2magic.content.engine.helper.EngineRegistryInstance;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.*;
import dev.xkmc.l2magic.content.engine.predicate.*;
import dev.xkmc.l2magic.content.engine.processor.*;
import dev.xkmc.l2magic.content.engine.selector.*;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.ArrowShoot;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.engine.TridentShoot;
import dev.xkmc.l2magic.content.entity.motion.MoveDeltaMotion;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.entity.renderer.OrientedRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.particle.engine.*;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class EngineRegistry {

	public static final ResourceKey<Registry<SpellAction>> SPELL = ResourceKey.createRegistryKey(L2Magic.loc("spell_action"));
	public static final ResourceKey<Registry<ProjectileConfig>> PROJECTILE = ResourceKey.createRegistryKey(L2Magic.loc("projectile"));

	public static final EngineRegistryInstance<EngineType<?>> ENGINE = EngineRegistryInstance.of("configured_engine");
	public static final EngineRegistryInstance<ModifierType<?>> MODIFIER = EngineRegistryInstance.of("modifier");
	public static final EngineRegistryInstance<PredicateType<?>> PREDICATE = EngineRegistryInstance.of("predicate");
	public static final EngineRegistryInstance<FilterType<?>> FILTER = EngineRegistryInstance.of("filter");
	public static final EngineRegistryInstance<SelectorType<?>> SELECTOR = EngineRegistryInstance.of("selector");
	public static final EngineRegistryInstance<ProcessorType<?>> PROCESSOR = EngineRegistryInstance.of("processor");
	public static final EngineRegistryInstance<MotionType<?>> MOTION = EngineRegistryInstance.of("motion");
	public static final EngineRegistryInstance<ParticleRenderType<?>> PARTICLE_RENDERER = EngineRegistryInstance.of("particle_renderer");
	public static final EngineRegistryInstance<ProjectileRenderType<?>> PROJECTILE_RENDERER = EngineRegistryInstance.of("projectile_renderer");

	private static final EngineReg REG = new EngineReg(L2Magic.REGISTRATE);

	public static final Val<ModifierType<ForwardOffsetModifier>> FORWARD = REG.reg("forward", () -> ForwardOffsetModifier.CODEC);
	public static final Val<ModifierType<NormalOffsetModifier>> NORMAL_OFFSET = REG.reg("normal_offset", () -> NormalOffsetModifier.CODEC);//TODO doc
	public static final Val<ModifierType<RotationModifier>> ROTATE = REG.reg("rotate", () -> RotationModifier.CODEC);
	public static final Val<ModifierType<OffsetModifier>> OFFSET = REG.reg("offset", () -> OffsetModifier.CODEC);
	public static final Val<ModifierType<SetPosModifier>> POSITION = REG.reg("set_position", () -> SetPosModifier.CODEC);//TODO doc
	public static final Val<ModifierType<SetDirectionModifier>> DIRECTION = REG.reg("direction", () -> SetDirectionModifier.CODEC);
	public static final Val<ModifierType<RandomOffsetModifier>> RANDOM_OFFSET = REG.reg("random_offset", () -> RandomOffsetModifier.CODEC);
	public static final Val<ModifierType<SetNormalModifier>> NORMAL = REG.reg("set_normal", () -> SetNormalModifier.CODEC);
	public static final Val<ModifierType<Dir2NormalModifier>> DIR_2_NORMAL = REG.reg("direction_to_normal", () -> Dir2NormalModifier.CODEC);
	public static final Val<ModifierType<Normal2DirModifier>> NORMAL_2_DIR = REG.reg("normal_to_direction", () -> Normal2DirModifier.CODEC);
	public static final Val<ModifierType<RandomDirModifier>> RAND_DIR = REG.reg("random_direction", () -> RandomDirModifier.CODEC);//TODO doc
	public static final Val<ModifierType<ToCurrentCasterPosModifier>> TO_CASTER_POS = REG.reg("move_to_caster", () -> ToCurrentCasterPosModifier.CODEC);
	public static final Val<ModifierType<ToCurrentCasterDirModifier>> TO_CASTER_DIR = REG.reg("align_with_caster", () -> ToCurrentCasterDirModifier.CODEC);

	public static final Val<EngineType<PredicateLogic>> IF = REG.reg("if", () -> PredicateLogic.CODEC);
	public static final Val<EngineType<ListLogic>> LIST = REG.reg("list", () -> ListLogic.CODEC);
	public static final Val<EngineType<DelayLogic>> DELAY = REG.reg("delay", () -> DelayLogic.CODEC);
	public static final Val<EngineType<RandomVariableLogic>> RANDOM = REG.reg("random", () -> RandomVariableLogic.CODEC);
	public static final Val<EngineType<VariableLogic>> VAR = REG.reg("variable", () -> VariableLogic.CODEC);
	public static final Val<EngineType<MoveEngine>> MOVE_ENGINE = REG.reg("move", () -> MoveEngine.CODEC);
	public static final Val<EngineType<ProcessorEngine>> PROCESS_ENGINE = REG.reg("processor", () -> ProcessorEngine.CODEC);
	public static final Val<EngineType<SyncedLogic>> SYNC = REG.reg("synced_logic", () -> SyncedLogic.CODEC);

	public static final Val<EngineType<LoopIterator>> ITERATE = REG.reg("iterate", () -> LoopIterator.CODEC);
	public static final Val<EngineType<DelayedIterator>> ITERATE_DELAY = REG.reg("iterate_delayed", () -> DelayedIterator.CODEC);
	public static final Val<EngineType<WhileDelayedIterator>> WHILE_DELAY = REG.reg("while_delayed", () -> WhileDelayedIterator.CODEC);//TODO doc
	public static final Val<EngineType<LinearIterator>> ITERATE_LINEAR = REG.reg("iterate_linear", () -> LinearIterator.CODEC);
	public static final Val<EngineType<RingIterator>> ITERATE_ARC = REG.reg("iterate_arc", () -> RingIterator.CODEC);
	public static final Val<EngineType<RingRandomIterator>> RANDOM_FAN = REG.reg("random_pos_fan", () -> RingRandomIterator.CODEC);
	public static final Val<EngineType<SphereRandomIterator>> RANDOM_SPHERE = REG.reg("random_pos_sphere", () -> SphereRandomIterator.CODEC);//TODO doc
	public static final Val<EngineType<BlockInRangeIterator>> BLOCK_IN_RANGE = REG.reg("block_in_range", () -> BlockInRangeIterator.CODEC);//TODO doc

	public static final Val<EngineType<SimpleParticleInstance>> SIMPLE_PARTICLE = REG.reg("particle", () -> SimpleParticleInstance.CODEC);//TODO doc
	public static final Val<EngineType<BlockParticleInstance>> BLOCK_PARTICLE = REG.reg("block_particle", () -> BlockParticleInstance.CODEC);//TODO doc
	public static final Val<EngineType<ItemParticleInstance>> ITEM_PARTICLE = REG.reg("item_particle", () -> ItemParticleInstance.CODEC);//TODO doc
	public static final Val<EngineType<DustParticleInstance>> DUST_PARTICLE = REG.reg("dust_particle", () -> DustParticleInstance.CODEC);//TODO doc
	public static final Val<EngineType<TransitionParticleInstance>> TRANSITION_PARTICLE = REG.reg("transition_particle", () -> TransitionParticleInstance.CODEC);//TODO doc
	public static final Val<EngineType<CustomParticleInstance>> CUSTOM_PARTICLE = REG.reg("custom_particle", () -> CustomParticleInstance.CODEC);//TODO doc

	public static final Val<EngineType<SoundInstance>> SOUND = REG.reg("sound", () -> SoundInstance.CODEC);//TODO doc

	public static final Val<EngineType<SetBlock>> SET_BLOCK = REG.reg("set_block", () -> SetBlock.CODEC);//TODO doc
	public static final Val<EngineType<SetBlockFacing>> SET_BLOCK_FACING = REG.reg("set_block_facing", () -> SetBlockFacing.CODEC);//TODO doc
	public static final Val<EngineType<RemoveBlock>> REMOVE_BLOCK = REG.reg("remove_block", () -> RemoveBlock.CODEC);//TODO doc
	public static final Val<EngineType<KnockBlock>> KNOCK_BLOCK = REG.reg("knock_block", () -> KnockBlock.CODEC);//TODO doc
	public static final Val<EngineType<ScheduleTick>> SCHEDULE_TICK = REG.reg("schedule_tick", () -> ScheduleTick.CODEC);//TODO doc

	public static final Val<EngineType<ArrowShoot>> ARROW = REG.reg("arrow", () -> ArrowShoot.CODEC);//TODO doc
	public static final Val<EngineType<TridentShoot>> TRIDENT = REG.reg("trident", () -> TridentShoot.CODEC);//TODO doc
	public static final Val<EngineType<CustomProjectileShoot>> CUSTOM_SHOOT = REG.reg("custom_projectile", () -> CustomProjectileShoot.CODEC);//TODO doc

	public static final Val<SelectorType<SelfSelector>> SELF = REG.reg("self", () -> SelfSelector.CODEC);
	public static final Val<SelectorType<MoveSelector>> MOVE_SELECTOR = REG.reg("move", () -> MoveSelector.CODEC);
	public static final Val<SelectorType<BoxSelector>> BOX = REG.reg("box", () -> BoxSelector.CODEC);
	public static final Val<SelectorType<CompoundEntitySelector>> COMPOUND = REG.reg("compound", () -> CompoundEntitySelector.CODEC);
	public static final Val<SelectorType<LinearCubeSelector>> LINEAR = REG.reg("line", () -> LinearCubeSelector.CODEC);
	public static final Val<SelectorType<ArcCubeSelector>> ARC = REG.reg("arc", () -> ArcCubeSelector.CODEC);
	public static final Val<SelectorType<ApproxCylinderSelector>> CYLINDER = REG.reg("cylinder", () -> ApproxCylinderSelector.CODEC);
	public static final Val<SelectorType<ApproxBallSelector>> BALL = REG.reg("ball", () -> ApproxBallSelector.CODEC);

	public static final Val<PredicateType<AndPredicate>> AND = REG.reg("and", () -> AndPredicate.CODEC);//TODO docs
	public static final Val<PredicateType<OrPredicate>> OR = REG.reg("or", () -> OrPredicate.CODEC);//TODO docs
	public static final Val<PredicateType<NotPredicate>> NOT = REG.reg("not", () -> NotPredicate.CODEC);//TODO docs
	public static final Val<PredicateType<MovePredicate>> MOVE_PRED = REG.reg("move", () -> MovePredicate.CODEC);//TODO docs
	public static final Val<PredicateType<BlockMatchCondition>> BLOCK_MATCH = REG.reg("block", () -> BlockMatchCondition.CODEC);//TODO docs
	public static final Val<PredicateType<SurfaceBelowCondition>> SURFACE_BELOW = REG.reg("surface_below", () -> SurfaceBelowCondition.CODEC);//TODO docs
	public static final Val<PredicateType<BlockTestCondition>> BLOCK_TEST = REG.reg("block_test", () -> BlockTestCondition.CODEC);//TODO docs
	public static final Val<PredicateType<DepthPredicate>> DEPTH = REG.reg("depth_test", () -> DepthPredicate.CODEC);//TODO docs
	public static final Val<PredicateType<BlockInRangePredicate>> RANGE = REG.reg("ranged_test", () -> BlockInRangePredicate.CODEC);//TODO docs

	public static final Val<FilterType<AndFilter>> AND_FILTER = REG.reg("and", () -> AndFilter.CODEC);//TODO docs
	public static final Val<FilterType<OrFilter>> OR_FILTER = REG.reg("or", () -> OrFilter.CODEC);//TODO docs
	public static final Val<FilterType<NotFilter>> NOT_FILTER = REG.reg("not", () -> NotFilter.CODEC);//TODO docs
	public static final Val<FilterType<MobEffectFilter>> EFFECT_FILTER = REG.reg("effect", () -> MobEffectFilter.CODEC);//TODO docs

	public static final Val<ProcessorType<DelayedProcessor>> DELAY_PROCESSOR = REG.reg("delay", () -> DelayedProcessor.CODEC);
	public static final Val<ProcessorType<DamageProcessor>> DAMAGE = REG.reg("damage", () -> DamageProcessor.CODEC);
	public static final Val<ProcessorType<KnockBackProcessor>> KB = REG.reg("knockback", () -> KnockBackProcessor.CODEC);
	public static final Val<ProcessorType<PushProcessor>> PUSH_ENTITY = REG.reg("push", () -> PushProcessor.CODEC);
	public static final Val<ProcessorType<EffectProcessor>> EFFECT = REG.reg("effect", () -> EffectProcessor.CODEC);
	public static final Val<ProcessorType<PropertyProcessor>> PROP = REG.reg("property", () -> PropertyProcessor.CODEC);
	public static final Val<ProcessorType<SetDeltaProcessor>> SET_DELTA = REG.reg("set_delta", () -> SetDeltaProcessor.CODEC);//TODO docs
	public static final Val<ProcessorType<TeleportProcessor>> TP = REG.reg("teleport", () -> TeleportProcessor.CODEC); //TODO docs
	public static final Val<ProcessorType<IgniteProcessor>> IGNITE = REG.reg("ignite", () -> IgniteProcessor.CODEC); //TODO docs
	public static final Val<ProcessorType<FilteredProcessor>> FILTERED = REG.reg("filtered", () -> FilteredProcessor.CODEC); //TODO docs
	public static final Val<ProcessorType<CastAtProcessor>> CAST_AT = REG.reg("cast_at", () -> CastAtProcessor.CODEC); //TODO docs

	public static final Val<MotionType<SimpleMotion>> SIMPLE_MOTION = REG.reg("simple", () -> SimpleMotion.CODEC); // TODO doc
	public static final Val<MotionType<MovePosMotion>> MOVE_MOTION = REG.reg("control_position", () -> MovePosMotion.CODEC);//TODO doc
	public static final Val<MotionType<MoveDeltaMotion>> DELTA_MOTION = REG.reg("control_velocity", () -> MoveDeltaMotion.CODEC);//TODO doc

	public static final Val<ParticleRenderType<SimpleParticleData>> SIMPLE_RENDER = REG.reg("simple", () -> SimpleParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<DustParticleData>> COLOR_RENDER = REG.reg("color", () -> DustParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<TransitionParticleData>> TRANSITION_RENDER = REG.reg("transition", () -> TransitionParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<BlockParticleData>> BLOCK_RENDER = REG.reg("block", () -> BlockParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<ItemParticleData>> ITEM_RENDER = REG.reg("item", () -> ItemParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<StaticTextureParticleData>> STATIC_RENDER = REG.reg("static", () -> StaticTextureParticleData.CODEC);//TODO doc
	public static final Val<ParticleRenderType<OrientedParticleData>> ORIENTED_RENDER = REG.reg("oriented", () -> OrientedParticleData.CODEC);//TODO doc

	public static final Val<ProjectileRenderType<OrientedRenderData>> PR_SIMPLE = REG.reg("simple", () -> OrientedRenderData.CODEC);//TODO doc


	public static void register() {

	}

}
