package dev.xkmc.l2magic.content.altar.recipe;

import dev.xkmc.l2magic.content.altar.tile.craft.CraftManager;
import net.minecraft.world.SimpleContainer;

public class AltarContainer extends SimpleContainer {

	public final CraftManager manager;

	public AltarContainer(CraftManager manager) {
		this.manager = manager;
	}
}
