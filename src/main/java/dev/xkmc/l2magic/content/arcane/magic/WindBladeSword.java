package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2magic.content.common.entity.WindBladeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class WindBladeSword extends Arcane {

	public final float dmg, velocity, dist;

	public WindBladeSword(float dmg, float velocity, float dist) {
		super(ArcaneType.ALIOTH, 2);
		this.dmg = dmg;
		this.velocity = velocity;
		this.dist = dist;
	}

	@Override
	public boolean activate(Player player, LLPlayerData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target != null)
			return false;
		float strength = player.getAttackStrengthScale(0.5f);
		if (strength < 0.9f)
			return false;
		player.resetAttackStrengthTicker();
		Level w = player.level;
		if (!w.isClientSide()) {
			WindBladeEntity e = new WindBladeEntity(w);
			e.setOwner(player);
			e.setPos(player.getX(), player.getEyeY() - 0.5f, player.getZ());
			e.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, 1);
			e.setProperties(dmg, Math.round(dist / velocity), (float) (Math.random() * 360f), stack);
			w.addFreshEntity(e);
		}
		return true;
	}
}
