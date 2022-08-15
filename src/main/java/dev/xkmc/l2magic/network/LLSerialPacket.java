package dev.xkmc.l2magic.network;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@SerialClass
public abstract class LLSerialPacket extends SerialPacketBase {


	public void toServer() {
		NetworkManager.HANDLER.toServer(this);
	}

	public void toTrackingPlayers(Entity e) {
		NetworkManager.HANDLER.toTrackingPlayers(this, e);
	}

	public void toClientPlayer(ServerPlayer e) {
		NetworkManager.HANDLER.toClientPlayer(this, e);
	}

	public void toAllClient() {
		NetworkManager.HANDLER.toAllClient(this);
	}

}
