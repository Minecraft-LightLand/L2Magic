package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThunderAxe extends Arcane {

	private final float radius;

	public ThunderAxe(int cost, float radius) {
		super(ArcaneType.MERAK, cost);
		this.radius = radius;
	}

	@Override
	public boolean activate(Player player, LLPlayerData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		strike(w, player, target);
		if (!w.isClientSide()) {
			search(w, player, radius, player.getPosition(1), target, true, this::strike);
		}
		return true;
	}

	private void strike(Level w, Player player, LivingEntity target) {
		BlockPos pos = target.blockPosition();
		if (!w.isClientSide()) {
			LightningBolt e = new LightningBolt(EntityType.LIGHTNING_BOLT, w);
			e.moveTo(Vec3.atBottomCenterOf(pos));
			e.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
			w.addFreshEntity(e);
			e.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 5f, 1.0F);
		}
	}

}
