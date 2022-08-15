package dev.xkmc.l2magic.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class LLClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(ClientRegister::registerOverlays);
		bus.addListener(ClientRegister::registerKeys);
		eventBus.addListener(ClientRegister::disableOverlays);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ClientRegister.registerItemProperties();
	}

}
