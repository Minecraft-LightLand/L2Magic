package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@SerialClass
public class OrderedConnector implements Connector {

	@SerialClass.SerialField(toClient = true)
	public TreeSet<BlockPos> set = new TreeSet<>(this::comparator);

	private final BlockEntity center;

	public OrderedConnector(BlockEntity center) {
		this.center = center;
	}

	@Override
	public List<BlockPos> target() {
		return new ArrayList<>(set);
	}

	@Override
	public NetworkType getNetworkType() {
		return NetworkType.ONE;
	}

	@Override
	public void link(BlockPos pos) {
		if (set.contains(pos)) set.remove(pos);
		else set.add(pos);
	}

	private int comparator(BlockPos a, BlockPos b) {
		return Double.compare(center.getBlockPos().distSqr(a), center.getBlockPos().distSqr(b));
	}

}
