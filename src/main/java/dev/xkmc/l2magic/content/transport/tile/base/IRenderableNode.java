package dev.xkmc.l2magic.content.transport.tile.base;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface IRenderableNode {

	int getMaxCoolDown();

	int getCoolDown();

	List<BlockPos> target();

	CoolDownType getType(BlockPos pos);

}
