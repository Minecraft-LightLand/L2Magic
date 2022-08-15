package dev.xkmc.l2magic.content.arcane.magic;

import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneType;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import dev.xkmc.l2magic.util.EffectAddUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MarkerSword extends Arcane {

	public final float radius;

	public MarkerSword(int cost, float radius) {
		super(ArcaneType.ALKAID, cost);
		this.radius = radius;
	}

	@Override
	public boolean activate(Player player, LLPlayerData magic, ItemStack stack, @Nullable LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		if (!w.isClientSide()) {
			search(w, player, radius, player.getPosition(1), target, false, (l, p, e) -> EffectAddUtil.addArcane(e, player));
		}
		return true;
	}
}
