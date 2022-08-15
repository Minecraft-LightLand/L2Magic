package dev.xkmc.l2magic.init;

import dev.xkmc.l2magic.content.common.render.MagicWandOverlay;
import dev.xkmc.l2magic.content.common.render.SpellBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class L2MagicClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(L2MagicClient::registerOverlays);
		bus.addListener(L2MagicClient::registerKeys);
		eventBus.addListener(L2MagicClient::disableOverlays);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		registerItemProperties();
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerItemProperties() {
	}

	@OnlyIn(Dist.CLIENT)
	public static void disableOverlays(RenderGuiOverlayEvent.Pre event) {
		if (event.getOverlay().id().equals(VanillaGuiOverlay.CROSSHAIR.id())) {
			event.setCanceled(true);
		}
		if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
			event.setCanceled(true);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "magic_wand", MagicWandOverlay.INSTANCE);
		event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "spell_bar", new SpellBarOverlay());
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerKeys(RegisterKeyMappingsEvent event) {
	}
}
