package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2foundation.init.registrate.LFEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.init.registrate.LLEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class EarthAxe extends Arcane {

	private final float radius;
	private final int time;

	public EarthAxe(float radius, int time) {
		super(ArcaneType.PHECDA, 0);
		this.radius = radius;
		this.time = time;
	}

	@Override
	public boolean activate(Player player, MagicData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		strike(w, player, target);
		if (!w.isClientSide()) {
			search(w, player, radius, player.getPosition(1), target, false, this::strike);
			EffectUtil.addEffect(target, new MobEffectInstance(LFEffects.HEAVY.get(), time, 1),
					EffectUtil.AddReason.SKILL, player);
			EffectUtil.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4),
					EffectUtil.AddReason.SKILL, player);
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			target.teleportTo(x, y - 1, z);
		}
		return true;
	}

	private void strike(Level w, Player player, LivingEntity target) {
		if (!w.isClientSide()) {
			EffectUtil.addEffect(target, new MobEffectInstance(LFEffects.HEAVY.get(), time, 0),
					EffectUtil.AddReason.SKILL, player);
			EffectUtil.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 2),
					EffectUtil.AddReason.SKILL, player);
		}
	}

}
