package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class DamageAxe extends Arcane {

	private final float damage;

	public DamageAxe(int cost, float damage) {
		super(ArcaneType.DUBHE, cost);
		this.damage = damage;
	}

	@Override
	public boolean activate(Player player, MagicData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		if (w.isClientSide())
			return true;
		DamageSource source = DamageSource.playerAttack(player);
		source.setMagic();
		source.bypassArmor();
		source.bypassMagic();
		target.hurt(source, damage);
		return true;
	}

}
