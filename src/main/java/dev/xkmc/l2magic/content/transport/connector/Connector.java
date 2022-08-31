package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import net.minecraft.core.BlockPos;

import java.util.List;

@SerialClass
public abstract class Connector {

	public abstract List<BlockPos> target();

	public abstract NetworkType getNetworkType();

}
