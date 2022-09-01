package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public interface Connector extends NetworkType {

	List<BlockPos> target();

	void link(BlockPos pos);

	void removeIf(Predicate<BlockPos> o);

}
