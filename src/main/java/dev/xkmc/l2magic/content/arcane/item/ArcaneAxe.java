package dev.xkmc.l2magic.content.arcane.item;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.arcane.internal.Arcane;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemCraftHelper;
import dev.xkmc.l2magic.content.arcane.internal.ArcaneItemUseHelper;
import dev.xkmc.l2magic.content.arcane.internal.IArcaneItem;
import dev.xkmc.l2magic.content.common.capability.player.LLPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ArcaneAxe extends AxeItem implements IArcaneItem {

	private final int mana;

	public ArcaneAxe(Tier tier, float attack, float speed, Properties props, int mana) {
		super(tier, attack, speed, props);
		this.mana = mana;
	}

	public static void add(ItemStack stack, List<Component> list) {
		List<Arcane> arcane = ArcaneItemCraftHelper.getAllArcanesOnItem(stack);
		Player pl = Proxy.getPlayer();
		LLPlayerData handler = pl == null ? null : LLPlayerData.get(pl);
		for (Arcane a : arcane) {
			boolean red = handler != null && !handler.magicAbility.isArcaneTypeUnlocked(a.type.get());
			MutableComponent text = a.type.get().getDesc();
			if (red)
				text.withStyle(ChatFormatting.RED);
			list.add(text.append(": ").append(a.getDesc()));
		}
	}

	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		add(stack, list);
	}

	public boolean isFoil(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null && tag.getBoolean("foil"))
			return true;
		return ArcaneItemUseHelper.isAxeCharged(stack);
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
