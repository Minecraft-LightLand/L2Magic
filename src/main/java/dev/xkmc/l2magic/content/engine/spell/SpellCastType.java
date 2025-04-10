package dev.xkmc.l2magic.content.engine.spell;

import dev.xkmc.l2magic.init.data.LMLangData;
import net.minecraft.network.chat.MutableComponent;

public enum SpellCastType {
	INSTANT("Instant Casting"),
	CHARGE("Charge Casting"),
	CONTINUOUS("Continuous Casting");

	private final String defDesc;

	SpellCastType(String defDesc) {
		this.defDesc = defDesc;
	}

	public MutableComponent desc() {
		return LMLangData.lang(this);
	}

	public String defDesc() {
		return defDesc;
	}
}
