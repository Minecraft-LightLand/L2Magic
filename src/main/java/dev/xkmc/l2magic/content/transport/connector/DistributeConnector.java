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

	@SerialClass.SerialField(toClient = true)
	private int id;

	public DistributeConnector(int max) {
		super(max);
	}

	@Override
	public List<BlockPos> getConnected() {
		return list;
	}

	@Override
	public List<BlockPos> getAvailableTarget() {
		if (list.isEmpty()) {
			return List.of();
		}
		id %= list.size();
		List<BlockPos> ans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ans.add(list.get((id + i) % list.size()));
		}
		return ans;
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
