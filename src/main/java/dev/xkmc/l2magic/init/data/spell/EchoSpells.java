package dev.xkmc.l2magic.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.Normal2DirModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.*;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

import java.util.List;

public class EchoSpells extends SpellDataGenEntry {

	public static final ResourceKey<SpellAction> RINGS = spell("echo_rings");
	public static final ResourceKey<SpellAction> BLADES = spell("echo_blades");

	@Override
	public void register(BootstapContext<SpellAction> ctx) {
		new SpellAction(
				verticalRings(new DataGenContext(ctx)),
				Items.ECHO_SHARD,
				400,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_ENTITY
		).verifyOnBuild(ctx, RINGS);

		new SpellAction(
				blades(new DataGenContext(ctx)),
				Items.SCULK_SHRIEKER,
				410,
				SpellCastType.INSTANT,
				SpellTriggerType.SELF_POS
		).verifyOnBuild(ctx, BLADES);
	}

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(RINGS.location()), "Echo Rings");
		pvd.add(SpellAction.lang(BLADES.location()), "Echo Blades");
	}

	private static ConfiguredEngine<?> blades(DataGenContext ctx) {
		int n = 3;
		int r = 2;
		int t = 30;
		String x = "CasterX+%1$s*cos(i*2*pi/%3$s)+%1$s*cos((i/%3$s+0.5)*2*pi+TickCount*2*pi/%2$s)".formatted(r, t, n);
		String z = "CasterZ+%1$s*sin(i*2*pi/%3$s)+%1$s*sin((i/%3$s+0.5)*2*pi+TickCount*2*pi/%2$s)".formatted(r, t, n);
		return new RingIterator(
				DoubleVariable.ZERO,
				DoubleVariable.of("-180"),
				DoubleVariable.of("180"),
				IntVariable.of("3"),
				false,
				new ListLogic(List.of(
						// TODO damage
						new CustomParticleInstance(
								DoubleVariable.ZERO,
								DoubleVariable.of("1.5"),
								IntVariable.of(t + ""),
								false,
								new MovePosMotion(List.of(
										new SetPosModifier(
												DoubleVariable.of(x),
												DoubleVariable.of("CasterY+1"),
												DoubleVariable.of(z)
										),
										new Normal2DirModifier()
								)),
								new OrientedParticleData(
										new StaticTextureParticleData(
												RenderTypePreset.LIT,
												ParticleTypes.SONIC_BOOM,
												10, 15
										),
										DoubleVariable.of("Age*6"),
										false
								)
						)
				)),
				"i"
		);
	}

	private static ConfiguredEngine<?> verticalRings(DataGenContext ctx) {
		return new ListLogic(List.of(
				//TODO damage
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("1"),
						new MoveEngine(List.of(
								new Normal2DirModifier(),
								ForwardOffsetModifier.of("i*0.2")
						), new CustomParticleInstance(
								DoubleVariable.ZERO,
								DoubleVariable.of("3"),
								IntVariable.of("16"),
								false,
								SimpleMotion.ZERO,
								new OrientedParticleData(
										new SimpleParticleData(
												RenderTypePreset.LIT,
												ParticleTypes.SONIC_BOOM
										),
										DoubleVariable.ZERO,
										true
								)
						)), "i"
				)
		));
	}

}
