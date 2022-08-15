package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2magic.init.registrate.LLEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class FireSword extends Arcane {

	private final float radius;
	private final int time;

	public FireSword(float radius, int time) {
		super(ArcaneType.ALIOTH, 0);
		this.radius = radius;
		this.time = time;
	}

	@Override
	public boolean activate(Player player, LLPlayerData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		strike(w, player, target);
		if (!w.isClientSide()) {
			search(w, player, radius, player.getPosition(1), target, false, this::strike);
			EffectUtil.addEffect(target, new MobEffectInstance(LLEffects.FLAME.get(), time, 1),
					EffectUtil.AddReason.SKILL, player);
		}
		return true;
	}

	private void strike(Level w, Player player, LivingEntity target) {
		if (!w.isClientSide()) {
			EffectUtil.addEffect(target, new MobEffectInstance(LLEffects.FLAME.get(), time, 0),
					EffectUtil.AddReason.SKILL, player);
		}
	}

}
