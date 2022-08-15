package dev.xkmc.l2magic.network;

import dev.xkmc.l2library.serial.config.ConfigMerger;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.PacketHandlerWithConfig;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.network.config.ProductTypeConfig;
import dev.xkmc.l2magic.network.config.SpellDataConfig;
import dev.xkmc.l2magic.network.config.SpellEntityConfig;
import dev.xkmc.l2magic.network.packets.CapToClient;
import dev.xkmc.l2magic.network.packets.CapToServer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.stream.Stream;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public class NetworkManager {

	static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(L2Magic.MODID, "main"), 1, "l2magic_config",
			e -> e.create(CapToClient.class, PLAY_TO_CLIENT),
			e -> e.create(CapToServer.class, PLAY_TO_SERVER)
	);

	public static Stream<Map.Entry<String, BaseConfig>> getConfigs(ConfigType type) {
		return HANDLER.getConfigs(type.getID());
	}

	public static <T extends BaseConfig> T getConfig(ConfigType type) {
		return HANDLER.getCachedConfig(type.getID());
	}

	public static void register() {
		addSimpleMapConfig(ConfigType.CONFIG_SPELL, SpellDataConfig.class);
		addSimpleMapConfig(ConfigType.PRODUCT_TYPE, ProductTypeConfig.class);
		addSimpleMapConfig(ConfigType.CONFIG_SPELL_ENTITY, SpellEntityConfig.class);
	}

	private static <C extends BaseConfig> void addSimpleMapConfig(ConfigType type, Class<C> con) {
		HANDLER.addCachedConfig(type.getID(), new ConfigMerger<>(con));
	}

}
