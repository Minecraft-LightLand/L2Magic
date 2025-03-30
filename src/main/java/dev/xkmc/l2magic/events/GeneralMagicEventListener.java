package dev.xkmc.l2magic.events;

import dev.xkmc.l2magic.content.engine.extension.ExtensionTypeManager;
import dev.xkmc.l2magic.content.item.utility.IMobClickItem;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = L2Magic.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GeneralMagicEventListener {

	@SubscribeEvent
	public static void onReload(TagsUpdatedEvent event) {
		event.getRegistryAccess().registryOrThrow(EngineRegistry.PROJECTILE)
				.holders().forEach(e -> e.value().verify(e.key().location()));
		event.getRegistryAccess().registryOrThrow(EngineRegistry.SPELL)
				.holders().forEach(e -> e.value().verify(e.key().location()));
		ExtensionTypeManager.reload();
	}

	@SubscribeEvent
	public static void onTargetCardClick(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof IMobClickItem) {
			if (event.getTarget() instanceof LivingEntity le) {
				event.setCancellationResult(event.getItemStack().interactLivingEntity(event.getEntity(),
						le, event.getHand()));
				event.setCanceled(true);
			}
		}
	}

}
