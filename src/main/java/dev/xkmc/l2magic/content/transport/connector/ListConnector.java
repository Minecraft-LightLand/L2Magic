package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SerialClass
public class ListConnector implements Connector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	private final boolean sync;

	public ListConnector(boolean sync) {
		this.sync = sync;
	}

	@Override
	public List<BlockPos> target() {
		return list;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		list.removeIf(o);
	}

	@Override
	public boolean testConsumption(int c) {
		return false;
	}

	@Override
	public boolean alwaysContinue() {
		return false;
	}

	@Override
	public int provide(int available, int consumed, int size) {
		if (sync) return 1;
		return Math.min(available - consumed, available / size);
	}

}
