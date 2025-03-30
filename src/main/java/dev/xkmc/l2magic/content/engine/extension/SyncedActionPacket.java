package dev.xkmc.l2magic.content.engine.extension;

import dev.xkmc.l2magic.content.engine.context.EngineContextData;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record SyncedActionPacket(
		ExtensionTypeKey type, EngineContextData data, int index
) implements SerialPacketBase<SyncedActionPacket> {

	@Override
	public void handle(Player player) {
		var ctx = data.create(player.level());
		if (ctx == null) return;
		var entry = SyncedActionEntry.get(data.root().value())
				.get(type.type(), index);
		if (!(entry instanceof SyncedAction<?> action)) return;
		ctx.execute(action.child());
		ctx.registerScheduler();
	}

}
