package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Optional;

public record EffectProcessor(
		MobEffect eff,
		IntVariable duration,
		IntVariable amplifier,
		boolean ambient, boolean visible
) implements EntityProcessor<EffectProcessor> {

	public static final Codec<EffectProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
			ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("effect").forGetter(e -> e.eff),
			IntVariable.codec("duration", e -> e.duration),
			IntVariable.optionalCodec("amplifier", e -> e.amplifier),
			Codec.BOOL.optionalFieldOf("ambient").forGetter(e -> Optional.of(e.ambient)),
			Codec.BOOL.optionalFieldOf("visible").forGetter(e -> Optional.of(e.visible))
	).apply(i, (a, b, c, d, e) -> new EffectProcessor(a, b,
			c.orElse(IntVariable.of("0")), d.orElse(false), e.orElse(true))));

	@Override
	public ProcessorType<EffectProcessor> type() {
		return EngineRegistry.EFFECT.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		int dur = duration.eval(ctx);
		int amp = amplifier.eval(ctx);
		for (var e : le) {
			EffectUtil.addEffect(e, new MobEffectInstance(eff, dur, amp, ambient, visible, true),
					EffectUtil.AddReason.NONE, ctx.user().user());
		}
	}

}
