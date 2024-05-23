package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;

public record DamageProcessor(
		Holder<DamageType> damageType,
		DoubleVariable damage,
		boolean indirect,
		boolean positioned
) implements EntityProcessor<DamageProcessor> {

	private static final Codec<Holder<DamageType>> DAMAGE_TYPE_CODEC =
			RegistryFileCodec.create(Registries.DAMAGE_TYPE, DamageType.CODEC);

	public static final Codec<DamageProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
			DAMAGE_TYPE_CODEC.fieldOf("damage_type").forGetter(e -> e.damageType),
			DoubleVariable.codec("damage", e -> e.damage),
			Codec.BOOL.optionalFieldOf("indirect").forGetter(e -> Optional.of(e.indirect)),
			Codec.BOOL.optionalFieldOf("positioned").forGetter(e -> Optional.of(e.positioned))
	).apply(i, (b, c, d, e) -> new DamageProcessor(b, c, d.orElse(false), e.orElse(true))));

	@Override
	public ProcessorType<DamageProcessor> type() {
		return EngineRegistry.DAMAGE.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel sl)) return;
		var user = ctx.user().user();
		DamageSource source = new DamageSource(damageType,
				indirect ? null : user, user,
				positioned ? ctx.loc().pos() : null);
		DamageSource alt = AttackEventHandler.onDamageSourceCreate(
				new CreateSourceEvent(sl.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE),
						damageType.unwrapKey().get(), user, source.getDirectEntity()));
		if (alt != null) source = alt;
		float dmg = (float) damage.eval(ctx);
		for (var e : le) {
			e.hurt(source, dmg);
		}
	}

}
