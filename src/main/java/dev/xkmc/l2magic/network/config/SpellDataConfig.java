package dev.xkmc.l2magic.network.config;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2magic.content.magic.spell.internal.Spell;
import dev.xkmc.l2magic.content.magic.spell.internal.SpellConfig;
import dev.xkmc.l2magic.network.ConfigType;
import dev.xkmc.l2magic.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

@SerialClass
public class SpellDataConfig extends BaseConfig {

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<String, SpellConfig> map = new HashMap<>();

	@Nullable
	public static <C extends SpellConfig> C getConfig(ResourceLocation rl) {
		SpellDataConfig config = NetworkManager.getConfig(ConfigType.CONFIG_SPELL);
		return Wrappers.cast(config.map.get(rl.toString()));
	}

	@DataGenOnly
	public SpellDataConfig add(Spell<?, ?> id, SpellConfig config) {
		map.put(id.getID(), config);
		return this;
	}

}
