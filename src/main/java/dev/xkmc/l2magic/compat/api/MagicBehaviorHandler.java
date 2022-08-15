package dev.xkmc.l2magic.compat.api;

import net.minecraft.world.entity.player.Player;

public class MagicBehaviorHandler implements MagicBehaviorListener {

	public int getArmorLoad(Player player) {
		return 0;
	}

	public boolean doLevelArcane(Player player) {
		return false;
	}

	public boolean unlockAll() {
		return true;
	}

	public double getDefaultMana() {
		return 100;
	}

	public double getDefaultLoad() {
		return 100;
	}

	public int getDefaultSpellSlot() {
		return 3;
	}

	public double getDefaultManaRestore() {
		return 0.01;
	}

	public double getDefaultLoadRestore() {
		return 0.01 / getDefaultSpellSlot();
	}
}
