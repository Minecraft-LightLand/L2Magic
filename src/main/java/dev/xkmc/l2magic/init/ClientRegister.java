package dev.xkmc.l2magic.init;

import dev.xkmc.l2magic.compat.GeneralCompatHandler;
import dev.xkmc.l2magic.content.common.render.MagicWandOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import java.util.HashSet;
import java.util.Set;

public class ClientRegister {

	public static final Set<ResourceLocation> REMOVED = new HashSet<>();

	@OnlyIn(Dist.CLIENT)
	public static void registerItemProperties() {
	}

	@OnlyIn(Dist.CLIENT)
	public static void disableOverlays(RenderGuiOverlayEvent.Pre event) {
		if (REMOVED.contains(event.getOverlay().id())) {
			event.setCanceled(true);//TODO
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		REMOVED.add(VanillaGuiOverlay.CROSSHAIR.id());
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "magic_wand", MagicWandOverlay.INSTANCE);
		GeneralCompatHandler.handle(GeneralCompatHandler.Stage.OVERLAY);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerKeys(RegisterKeyMappingsEvent event) {
	}

}
