package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class ListConnector implements Connector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	private final NetworkType type;

	public ListConnector(NetworkType type){
		this.type = type;
	}

	@Override
	public List<BlockPos> target() {
		return list;
	}

	@Override
	public NetworkType getNetworkType() {
		return type;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
	}

}
