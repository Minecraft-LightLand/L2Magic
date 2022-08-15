package dev.xkmc.l2magic.events;

import dev.xkmc.l2magic.network.packets.EmptyRightClickToServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ItemUseEventHandler {

	public static final List<ItemClickHandler> LIST = new ArrayList<>();

	public static <T extends PlayerEvent> void execute(ItemStack stack, T event, TriCon<T> cons) {
		if (stack.getItem() instanceof ItemClickHandler && ((ItemClickHandler) stack.getItem()).predicate(stack, event.getClass(), event))
			cons.accept((ItemClickHandler) stack.getItem(), stack, event);
		for (ItemClickHandler handler : LIST)
			if (handler.predicate(stack, event.getClass(), event))
				cons.accept(handler, stack, event);
	}

	@SubscribeEvent
	public static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
		if (event.getEntity().level.isClientSide()) {
			new EmptyRightClickToServer(false, event.getHand() == InteractionHand.MAIN_HAND).toServer();
		}
		execute(event.getItemStack(), event, ItemClickHandler::onPlayerLeftClickEmpty);
	}

	@SubscribeEvent
	public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		execute(event.getItemStack(), event, ItemClickHandler::onPlayerLeftClickBlock);
	}

	@SubscribeEvent
	public static void onPlayerLeftClickEntity(AttackEntityEvent event) {
		execute(event.getEntity().getMainHandItem(), event, ItemClickHandler::onPlayerLeftClickEntity);
	}

	@SubscribeEvent
	public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
		if (event.getEntity().level.isClientSide()) {
			new EmptyRightClickToServer(true, event.getHand() == InteractionHand.MAIN_HAND).toServer();
		}
		execute(event.getEntity().getItemInHand(event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND), event, ItemClickHandler::onPlayerRightClickEmpty);
	}

	@SubscribeEvent
	public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		execute(event.getItemStack(), event, ItemClickHandler::onPlayerRightClickBlock);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		execute(event.getItemStack(), event, ItemClickHandler::onPlayerRightClickEntity);
	}

	@SubscribeEvent
	public static void onCriticalHit(CriticalHitEvent event) {
		execute(event.getEntity().getMainHandItem(), event, ItemClickHandler::onCriticalHit);
	}

	public interface ItemClickHandler {

		boolean predicate(ItemStack stack, Class<? extends PlayerEvent> cls, PlayerEvent event);

		default void onPlayerLeftClickEmpty(ItemStack stack, PlayerInteractEvent.LeftClickEmpty event) {

		}

		default void onPlayerLeftClickBlock(ItemStack stack, PlayerInteractEvent.LeftClickBlock event) {

		}

		default void onPlayerLeftClickEntity(ItemStack stack, AttackEntityEvent event) {

		}

		default void onCriticalHit(ItemStack stack, CriticalHitEvent event) {

		}

		default void onPlayerRightClickEmpty(ItemStack stack, PlayerInteractEvent.RightClickEmpty event) {

		}

		default void onPlayerRightClickBlock(ItemStack stack, PlayerInteractEvent.RightClickBlock event) {

		}

		default void onPlayerRightClickEntity(ItemStack stack, PlayerInteractEvent.EntityInteract event) {

		}

	}

	@FunctionalInterface
	public interface TriCon<T> {

		void accept(ItemClickHandler handler, ItemStack stack, T event);

	}

}
