package dev.xkmc.l2magic.content.transport.tile.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRenderableNode {

	int getMaxCoolDown();

	int getCoolDown();

	List<BlockPos> target();

	CoolDownType getType(BlockPos pos);

}
