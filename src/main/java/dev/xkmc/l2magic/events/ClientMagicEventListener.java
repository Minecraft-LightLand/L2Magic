package dev.xkmc.l2magic.events;

import dev.xkmc.fastprojectileapi.render.ProjectileRenderHelper;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.item.utility.IMobClickItem;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Objects;

import static net.neoforged.neoforge.event.TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED;

@EventBusSubscriber(modid = L2Magic.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientMagicEventListener {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onReload(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == CLIENT_PACKET_RECEIVED) {
			event.getRegistryAccess().registryOrThrow(EngineRegistry.PROJECTILE)
					.holders().map(e -> e.value().renderer())
					.filter(Objects::nonNull)
					.forEach(ProjectileRenderData::buildRenderer);
			ProjectileRenderHelper.setup();
		}
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
