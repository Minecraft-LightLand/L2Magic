package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SerialClass
public class DistributeConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	@SerialClass.SerialField
	private int id;

	public DistributeConnector(int max) {
		super(max);
	}

	@Override
	public List<BlockPos> target() {
		id %= list.size();
		return List.of(list.get(id));
	}

	@Override
	public void perform() {
		super.perform();
		id++;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
		id = 0;
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		list.removeIf(o);
		id = 0;
	}

	@Override
	public boolean shouldContinue(int available, int consumed, int size) {
		return consumed == 0 && super.shouldContinue(available, consumed, size);
	}
}
