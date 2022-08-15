package dev.xkmc.l2magic.init.data;

import net.minecraft.network.chat.MutableComponent;

public enum Lore {
	ENCHANT_LOAD("enchant_load", "This piece of armor is over-enchanted. Wearers will be cursed");

	final String id;
	final String lore;

	Lore(String id, String lore) {
		this.id = id;
		this.lore = lore;
	}

	public MutableComponent get() {
		return LangData.translate("lore.lightland." + id);
	}

}
