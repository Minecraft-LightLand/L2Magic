package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SyncedConnector implements Connector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	@Override
	public List<BlockPos> target() {
		return list;
	}

	@Override
	public NetworkType getNetworkType() {
		return NetworkType.ALL;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
	}

}
