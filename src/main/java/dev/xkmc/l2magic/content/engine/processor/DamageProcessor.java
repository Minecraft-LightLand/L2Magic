package dev.xkmc.l2magic.content.engine.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
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
) implements SimpleServerProcessor<DamageProcessor> {

	private static final Codec<Holder<DamageType>> DAMAGE_TYPE_CODEC =
			RegistryFileCodec.create(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC, false);

	public static final MapCodec<DamageProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
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
	public void process(SelectedEntities le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel sl)) return;
		var user = ctx.user().user();
		DamageSource source = AttackEventHandler.createSource(sl, user, damageType.unwrapKey().orElseThrow(),
				indirect ? null : user, positioned ? ctx.loc().pos() : null);
		float dmg = (float) damage.eval(ctx);
		if (source.is(DamageTypeTags.IS_PROJECTILE)) {
			var ins = user.getAttribute(L2DamageTracker.BOW_STRENGTH);
			if (ins != null) dmg *= (float) ins.getValue();
		}
		for (var e : le.entries()) {
			e.root().hurt(source, dmg);
		}
	}

}
