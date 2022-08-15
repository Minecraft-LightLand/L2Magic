package dev.xkmc.l2magic.content.magic.item;

import dev.xkmc.l2magic.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionCore extends Item {

	public PotionCore(Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		list.add(MagicScroll.getTarget(stack).text());
		list.add(LangData.IDS.POTION_RADIUS.get(MagicScroll.getRadius(stack)));
		PotionUtils.addPotionTooltip(stack, list, 1);
	}

	public boolean isFoil(ItemStack stack) {
		return true;
	}

}
