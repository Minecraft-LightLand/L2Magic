package dev.xkmc.l2magic.content.arcane.item;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.content.arcane.internal.IArcaneItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ArcaneSword extends SwordItem implements IArcaneItem {

	private final int mana;

	public ArcaneSword(Tier tier, int attack, float speed, Properties props, int mana) {
		super(tier, attack, speed, props);
		this.mana = mana;
	}

	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		ArcaneAxe.add(stack, list);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.getBoolean("foil"))
			return true;
		return super.isFoil(stack);
	}

	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
		return true;
	}

	public boolean mineBlock(ItemStack stack, Level w, BlockState state, BlockPos pos, LivingEntity user) {
		return true;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return (int) Math.round(13.0 * ArcaneItemUseHelper.getArcaneMana(stack) / getMaxMana(stack));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0xFFFFFF;
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return mana;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity user, int slot, boolean selected) {
		if (user instanceof Player player && selected) {
			RayTraceUtil.clientUpdateTarget(player, getDistance(stack));
		}
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

}
