package dev.xkmc.l2magic.network;

import java.util.Locale;

public enum ConfigType {
	PRODUCT_TYPE, MAGIC_DATA, CONFIG_SPELL, CONFIG_SPELL_ENTITY;

	public String getID() {
		return name().toLowerCase(Locale.ROOT);
	}

}
