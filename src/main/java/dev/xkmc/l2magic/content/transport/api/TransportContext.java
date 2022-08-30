package dev.xkmc.l2magic.content.transport.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TransportContext<T> {

	private final Map<BlockPos, List<BlockPos>> set = new TreeMap<>();
	private final Stack<BlockPos> stack = new Stack<>();

	@Nullable
	private Pair<List<BlockPos>, List<BlockPos>> collision = null;

	TransportContext() {

	}

	public void push(BlockPos id) {
		stack.push(id);
		if (set.containsKey(id)) {
			collision = Pair.of(set.get(id), new ArrayList<>(stack));
			return;
		}
		set.put(id, new ArrayList<>(stack));
	}

	public boolean hasError() {
		return collision != null;
	}

	public void pop() {
		stack.pop();
	}

	public boolean evaluate(BlockPos from, BlockPos to) {
		return false;//TODO
	}

}
