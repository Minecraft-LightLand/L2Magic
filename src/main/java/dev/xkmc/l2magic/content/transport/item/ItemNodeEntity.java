package dev.xkmc.l2magic.content.transport.item;

import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemNodeEntity {

	boolean isItemStackValid(ItemStack stack);

	NetworkType getNetworkType();

	@Nullable
	Level getLevel();

	List<BlockPos> target();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	BlockPos getBlockPos();

	boolean isReady();

}
