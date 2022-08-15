package dev.xkmc.l2magic.network.packets;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.content.common.capability.MagicAbility;
import dev.xkmc.l2magic.network.LLSerialPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Function;

@SerialClass
public class CapToClient extends LLSerialPacket {

	@SerialClass.SerialField
	public Action action;

	@SerialClass.SerialField
	public CompoundTag tag;

	@Deprecated
	public CapToClient() {

	}

	public CapToClient(Action action, MagicData handler) {
		this.action = action;
		this.tag = action.server.apply(handler);
	}

	public void handle(NetworkEvent.Context context) {
		if (action != Action.ALL && action != Action.CLONE && !Proxy.getClientPlayer().isAlive())
			return;
		action.client.accept(tag);
	}

	public static void reset(ServerPlayer e, MagicData.Reset reset) {
		CapToClient msg = new CapToClient(Action.RESET, null);
		msg.tag.putInt("ordinal", reset.ordinal());
		msg.toClientPlayer(e);
	}

	public enum Action {
		DEBUG((m) -> TagCodec.toTag(new CompoundTag(), m), (tag) -> {
			MagicData m = MagicData.getClientAccess();
			CompoundTag comp = Wrappers.get(() -> TagCodec.toTag(new CompoundTag(), MagicData.class, m, f -> true));
			CapToServer.sendDebugInfo(tag, comp);
		}),
		ALL((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m);
		}, tag -> MagicData.HOLDER.cacheSet(tag, false)),
		CLONE((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m);
		}, tag -> MagicData.HOLDER.cacheSet(tag, true)),
		ARCANE_TYPE((m) -> m.magicAbility.arcane_type, (tag) -> {
			MagicAbility abi = MagicData.getClientAccess().magicAbility;
			abi.arcane_type = tag;
		}), MAGIC_ABILITY((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m.magicAbility);
		}, (tag) -> {
			MagicData h = MagicData.getClientAccess();
			h.magicAbility = new MagicAbility(h);
			Wrappers.run(() -> TagCodec.fromTag(tag, MagicAbility.class, h.magicAbility, f -> true));
			h.reInit();
		}), RESET(m -> new CompoundTag(), tag -> {
			MagicData h = MagicData.getClientAccess();
			h.reset(MagicData.Reset.values()[tag.getInt("ordinal")]);
		});

		public final Function<MagicData, CompoundTag> server;
		public final Consumer<CompoundTag> client;


		Action(Function<MagicData, CompoundTag> server, Consumer<CompoundTag> client) {
			this.server = server;
			this.client = client;
		}
	}

}
