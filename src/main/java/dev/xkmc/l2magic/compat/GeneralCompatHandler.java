package dev.xkmc.l2magic.compat;

import net.minecraftforge.fml.ModList;

public class GeneralCompatHandler {

	public enum Stage {
		INIT,
		OVERLAY
	}

	public static void handle(Stage stage) {
		if (ModList.get().isLoaded("create")) {
			//TODO Wrappers.ignore(() -> CreateCompatHandler.handleCompat(stage));
		}
	}

}
