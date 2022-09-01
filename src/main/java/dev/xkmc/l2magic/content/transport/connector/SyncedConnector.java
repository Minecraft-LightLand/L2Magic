package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SerialClass
public class SyncedConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	public SyncedConnector(int max) {
		super(max);
	}

	@Override
	public List<BlockPos> getConnected() {
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
	public boolean testConsumption(int avail, int c) {
		return avail == c;
	}

	@Override
	public boolean shouldContinue(int available, int consumed, int size) {
		return true;
	}

	@Override
	public int provide(int available, int consumed, int size) {
		return 1;
	}

}
