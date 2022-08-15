package dev.xkmc.l2magic.compat.api;

import net.minecraft.world.entity.player.Player;

public interface MagicBehaviorListener {

	MagicBehaviorListener INSTANCE = new MagicBehaviorHandler();

	int getArmorLoad(Player player);

	boolean doLevelArcane(Player player);

	boolean unlockAll();

	double getDefaultMana();

	double getDefaultLoad();

	int getDefaultSpellSlot();

	double getDefaultManaRestore();

	double getDefaultLoadRestore();

}
