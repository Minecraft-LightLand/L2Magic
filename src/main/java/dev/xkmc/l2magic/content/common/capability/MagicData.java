package dev.xkmc.l2magic.content.common.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.network.packets.CapToClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.function.Consumer;

@SerialClass
public class MagicData extends PlayerCapabilityTemplate<MagicData> {

	public static final Capability<MagicData> CAPABILITY = CapabilityManager.get(new CapabilityToken<MagicData>() {
	});

	public static final PlayerCapabilityHolder<MagicData> HOLDER = new PlayerCapabilityHolder<>(
			new ResourceLocation(L2Magic.MODID, "player_data"), CAPABILITY,
			MagicData.class, MagicData::new, holder -> new PlayerCapabilityNetworkHandler<>(holder) {

		@Override
		public void toClientSyncAll(ServerPlayer e) {
			new CapToClient(CapToClient.Action.ALL, HOLDER.get(e)).toClientPlayer(e);
		}

		@Override
		public void toClientSyncClone(ServerPlayer e) {
			new CapToClient(CapToClient.Action.CLONE, HOLDER.get(e)).toClientPlayer(e);
		}
	});

	public static MagicData get(Player player) {
		return HOLDER.get(player);
	}

	public static boolean isProper(Player player) {
		return HOLDER.isProper(player);
	}

	@OnlyIn(Dist.CLIENT)
	public static MagicData getClientAccess() {
		return get(Proxy.getClientPlayer());
	}

	public static void register() {

	}

	@SerialClass.SerialField
	public State state = State.PREINJECT;
	@SerialClass.SerialField
	public MagicAbility magicAbility = new MagicAbility(this);
	@SerialClass.SerialField
	public MagicHolder magicHolder = new MagicHolder(this);

	public void tick() {
		magicAbility.tick();
	}

	public void reset(Reset reset) {
		reset.cons.accept(this);
	}

	public void init() {
		if (state == null) {
			reset(Reset.FOR_INJECT);
		}
		if (state != State.ACTIVE) {
			magicHolder.checkUnlocks();
			state = State.ACTIVE;
		}
	}

	public void reInit() {
		state = State.PREINIT;
		check();
	}

	public MagicData check() {
		if (state != State.ACTIVE)
			init();
		return this;
	}

	@Override
	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			magicAbility.magic_mana = magicAbility.getMaxMana();
			magicAbility.spell_load = 0;
		}
	}

	@Deprecated
	@Override
	public void preInject() {
		reset(Reset.FOR_INJECT);
	}

	@Deprecated
	@SuppressWarnings("unused")
	@SerialClass.OnInject
	public void onInject() {
		if (state == State.PREINJECT || state == State.ACTIVE)
			state = State.PREINIT;
	}


	public enum State {
		PREINJECT, PREINIT, ACTIVE
	}

	public enum Reset {
		ABILITY((h) -> {
			h.magicAbility = new MagicAbility(h);
		}), HOLDER((h) -> {
			h.magicHolder = new MagicHolder(h);
			h.magicHolder.checkUnlocks();
		}), SKILL(h -> {
		}),
		ALL((h) -> {
			ABILITY.cons.accept(h);
			HOLDER.cons.accept(h);
			SKILL.cons.accept(h);
		}), FOR_INJECT((h) -> {
			h.state = State.PREINJECT;
			h.magicAbility = new MagicAbility(h);
			h.magicHolder = new MagicHolder(h);
		});

		final Consumer<MagicData> cons;

		Reset(Consumer<MagicData> cons) {
			this.cons = cons;
		}
	}

}
