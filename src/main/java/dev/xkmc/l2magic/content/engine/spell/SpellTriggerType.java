package dev.xkmc.l2magic.content.engine.spell;

import dev.xkmc.l2magic.init.data.LMLangData;
import net.minecraft.network.chat.MutableComponent;

public enum SpellTriggerType {
	SELF_POS("Cast at user position"),
	AXIS_ALIGNED_FACING("Cast toward axis directions"),
	HORIZONTAL_FACING("Cast toward horizontal directions"),
	FACING_FRONT("Cast forward"),
	FACING_BACK("Cast forward"),
	TARGET_POS("Cast at target position"),
	TARGET_ENTITY("Cast at target entity");

	private final String defDesc;

	SpellTriggerType(String defDesc) {
		this.defDesc = defDesc;
	}

	public MutableComponent desc() {
		return LMLangData.lang(this);
	}

	public String defDesc() {
		return defDesc;
	}
}
