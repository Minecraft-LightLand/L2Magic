package dev.xkmc.l2magic.content.transport.api;

import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.TreeSet;

public class TransportContext<T> {

	private final Set<BlockPos> set = new TreeSet<>();

	TransportContext() {

	}

	public boolean add(BlockPos id) {
		return set.add(id);
	}

}
