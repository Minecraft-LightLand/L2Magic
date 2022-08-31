package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class SimpleConnector extends Connector {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public BlockPos pos = null;

	@Override
	public List<BlockPos> target() {
		return pos == null ? List.of() : List.of(pos);
	}

	@Override
	public NetworkType getNetworkType() {
		return NetworkType.ONE;
	}
}
