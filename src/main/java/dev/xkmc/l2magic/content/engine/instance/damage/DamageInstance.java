package dev.xkmc.l2magic.content.engine.instance.damage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

import java.util.Optional;

public record DamageInstance(
		AxialEntitySelector selector,
		Holder<DamageType> damageType,
		DoubleVariable damage,
		boolean indirect,
		boolean positioned
) implements ConfiguredEngine<DamageInstance> {

	private static final Codec<Holder<DamageType>> DAMAGE_TYPE_CODEC =
			RegistryFileCodec.create(Registries.DAMAGE_TYPE, DamageType.CODEC);

	public static final Codec<DamageInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			AxialEntitySelector.CODEC.fieldOf("selector").forGetter(e -> e.selector),
			DAMAGE_TYPE_CODEC.fieldOf("damage_type").forGetter(e -> e.damageType),
			DoubleVariable.codec("damage", e -> e.damage),
			Codec.BOOL.optionalFieldOf("indirect").forGetter(e -> Optional.of(e.indirect)),
			Codec.BOOL.optionalFieldOf("positioned").forGetter(e -> Optional.of(e.positioned))
	).apply(i, (a, b, c, d, e) -> new DamageInstance(a, b, c, d.orElse(false), e.orElse(true))));

	@Override
	public EngineType<DamageInstance> type() {
		return EngineRegistry.DAMAGE.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel sl)) return;
		var user = ctx.user().user();
		DamageSource source = new DamageSource(damageType, indirect ? null : user, user, positioned ? ctx.loc().pos() : null);
		for (var e : selector().find(sl, ctx)) {
			//e.hurt(
		}

	}

}
