package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

@SerialClass
public class OrderedConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public TreeSet<BlockPos> set = new TreeSet<>(this::comparator);

	private final BlockEntity center;

	public OrderedConnector(BlockEntity center, int max) {
		super(max);
		this.center = center;
	}

	@Override
	public List<BlockPos> getConnected() {
		return new ArrayList<>(set);
	}

	@Override
	public void link(BlockPos pos) {
		if (set.contains(pos)) set.remove(pos);
		else set.add(pos);
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		set.removeIf(o);
	}

	private int comparator(BlockPos a, BlockPos b) {
		return Double.compare(center.getBlockPos().distSqr(a), center.getBlockPos().distSqr(b));
	}

}
