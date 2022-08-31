package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface Connector {

	List<BlockPos> target();

	NetworkType getNetworkType();

	void link(BlockPos pos);
}
