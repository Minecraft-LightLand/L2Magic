package dev.xkmc.l2magic.events;

import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Magic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GeneralMagicEventListener {

	@SubscribeEvent
	public static void onReload(TagsUpdatedEvent event) {
		event.getRegistryAccess().registryOrThrow(EngineRegistry.PROJECTILE)
				.holders().forEach(e -> e.get().verify(e.key().location()));
		event.getRegistryAccess().registryOrThrow(EngineRegistry.SPELL)
				.holders().forEach(e -> e.get().verify(e.key().location()));
	}

}
