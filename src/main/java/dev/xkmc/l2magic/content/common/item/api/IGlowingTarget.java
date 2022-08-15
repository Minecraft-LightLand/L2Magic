package dev.xkmc.l2magic.content.common.item.api;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IGlowingTarget {

	@OnlyIn(Dist.CLIENT)
	int getDistance(ItemStack stack);

}
