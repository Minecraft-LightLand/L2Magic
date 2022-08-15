package dev.xkmc.l2magic.network.config;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2magic.content.magic.render.SpellComponent;
import dev.xkmc.l2magic.network.ConfigType;
import dev.xkmc.l2magic.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

@SerialClass
public class SpellEntityConfig extends BaseConfig {

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<String, SpellComponent> map = new HashMap<>();

	@Nullable
	public static SpellComponent getConfig(ResourceLocation rl) {
		SpellEntityConfig config = NetworkManager.getConfig(ConfigType.CONFIG_SPELL_ENTITY);
		return Wrappers.cast(config.map.get(rl.toString()));
	}

	@DataGenOnly
	public SpellEntityConfig add(ResourceLocation id, SpellComponent comp) {
		map.put(id.toString(), comp);
		return this;
	}
}
