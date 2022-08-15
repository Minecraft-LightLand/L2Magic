package dev.xkmc.l2magic.init.data.configs;

import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.ConfigDataProvider;
import dev.xkmc.l2magic.network.ConfigType;
import net.minecraft.data.DataGenerator;

import java.util.Map;

public class ConfigGenDispatcher extends ConfigDataProvider {

	public ConfigGenDispatcher(DataGenerator generator) {
		super(generator, "data/l2magic/l2magic_config/", "L2Magic Json Config Generator");
	}

	@Override
	public void add(Map<String, BaseConfig> map) {
		MagicConfigGen.addProductType((id, config) -> map.put(ConfigType.PRODUCT_TYPE.getID() + "/" + id, config));
		MagicRecipeGen.addRecipe((id, config) -> map.put(ConfigType.MAGIC_DATA.getID() + "/" + id, config));
		SpellConfigGen.add((id, config) -> map.put(ConfigType.CONFIG_SPELL.getID() + "/" + id, config));
		SpellRenderGen.add((id, config) -> map.put(ConfigType.CONFIG_SPELL_ENTITY.getID() + "/" + id, config));
	}

}
