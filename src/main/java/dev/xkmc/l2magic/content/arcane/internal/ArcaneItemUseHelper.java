package dev.xkmc.l2magic.content.arcane.internal;

import dev.xkmc.l2foundation.events.ItemUseEventHandler;
import dev.xkmc.l2library.util.annotation.DoubleSidedCall;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2magic.content.arcane.item.ArcaneAxe;
import dev.xkmc.l2magic.content.arcane.item.ArcaneSword;
import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.init.special.LightLandRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

public class ArcaneItemUseHelper implements ItemUseEventHandler.ItemClickHandler {

	public static boolean isArcaneItem(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof ArcaneSword || item instanceof ArcaneAxe;
	}

	@DoubleSidedCall
	public static boolean executeArcane(Player player, MagicData magic,
										ItemStack stack, ArcaneType type, @Nullable LivingEntity target) {
		if (!magic.magicAbility.isArcaneTypeUnlocked(type))
			return false;
		CompoundTag tag = stack.getTagElement("arcane");
		if (tag == null || !tag.contains(type.getID()))
			return false;
		String str = tag.getString(type.getID());
		ResourceLocation rl = new ResourceLocation(str);
		Arcane arcane = LightLandRegistry.ARCANE.get().getValue(rl);
		if (arcane == null || arcane.cost > tag.getInt("mana"))
			return false;
		if (arcane.activate(player, magic, stack, target)) {
			if (!player.level.isClientSide())
				tag.putInt("mana", tag.getInt("mana") - arcane.cost);
			return true;
		}
		return false;
	}

	@ServerOnly
	public static void rightClickAxe(Level level, ItemStack stack) {
		if (level.isClientSide()) return;
		CompoundTag tag = stack.getOrCreateTagElement("arcane");
		tag.putBoolean("charged", !tag.getBoolean("charged"));
	}

	public static boolean isAxeCharged(ItemStack stack) {
		return stack.getOrCreateTagElement("arcane").getBoolean("charged");
	}

	@ServerOnly
	public static void addArcaneMana(ItemStack stack, int mana) {
		IArcaneItem item = (IArcaneItem) stack.getItem();
		CompoundTag tag = stack.getOrCreateTagElement("arcane");
		tag.putInt("mana", Mth.clamp(tag.getInt("mana") + mana, 0, item.getMaxMana(stack)));
	}

	public static int getArcaneMana(ItemStack stack) {
		return stack.getOrCreateTagElement("arcane").getInt("mana");
	}

	/**
	 * executes on server only, play animation in client
	 */
	@ServerOnly
	private static void handleLeftClickEvent(ItemStack stack, PlayerInteractEvent event, @Nullable LivingEntity target) {
		Player player = event.getEntity();
		MagicData magic = MagicData.get(player);
		if (stack.getItem() instanceof ArcaneAxe) {
			ArcaneType type = isAxeCharged(stack) ? ArcaneType.DUBHE.get() : ArcaneType.MEGREZ.get();
			if (executeArcane(player, magic, stack, type, target)) {
				if (event.isCancelable())
					event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		} else if (stack.getItem() instanceof ArcaneSword) {
			if (executeArcane(player, magic, stack, ArcaneType.ALIOTH.get(), target)) {
				if (event.isCancelable())
					event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
	}

	/**
	 * executes on server only, play animation in client
	 */
	@ServerOnly
	private static void handleRightClickEvent(ItemStack stack, PlayerInteractEvent event, @Nullable LivingEntity target) {
		boolean cancellable = event.isCancelable();
		if (stack.getItem() instanceof ArcaneAxe) {
			rightClickAxe(event.getLevel(), stack);
			if (cancellable) event.setCanceled(true);
			event.setCancellationResult(InteractionResult.SUCCESS);
		} else if (stack.getItem() instanceof ArcaneSword) {
			if (executeArcane(event.getEntity(),
					MagicData.get(event.getEntity()),
					stack, ArcaneType.ALKAID.get(), target)) {
				if (cancellable) event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
	}

	@Nullable
	public static LivingEntity toLiving(Entity e) {
		return e instanceof LivingEntity ? (LivingEntity) e : null;
	}

	@Override
	public boolean predicate(ItemStack stack, Class<? extends PlayerEvent> cls, PlayerEvent event) {
		return isArcaneItem(stack);
	}

	@Override
	public void onPlayerLeftClickEmpty(ItemStack stack, PlayerInteractEvent.LeftClickEmpty event) {
		handleLeftClickEvent(stack, event, null);
	}

	@Override
	public void onPlayerLeftClickBlock(ItemStack stack, PlayerInteractEvent.LeftClickBlock event) {
		handleLeftClickEvent(stack, event, null);
	}

	@DoubleSidedCall
	@Override
	public void onPlayerLeftClickEntity(ItemStack stack, AttackEntityEvent event) {
	}

	@ServerOnly
	@Override
	public void onCriticalHit(ItemStack stack, CriticalHitEvent event) {
		if (event.getEntity().level.isClientSide())
			return;
		Player player = event.getEntity();
		MagicData magic = MagicData.get(player);
		LivingEntity le = toLiving(event.getTarget());
		ArcaneType type = null;
		boolean cr = event.isVanillaCritical();
		if (stack.getItem() instanceof ArcaneAxe) {
			boolean ch = isAxeCharged(stack);
			type = cr ? ch ? ArcaneType.MERAK.get() : ArcaneType.PHECDA.get() : ch ? ArcaneType.DUBHE.get() : ArcaneType.MEGREZ.get();
		} else if (stack.getItem() instanceof ArcaneSword) {
			type = cr ? ArcaneType.MIZAR.get() : ArcaneType.ALIOTH.get();
		}
		if (type != null)
			executeArcane(player, magic, stack, type, le);
	}

	@Override
	public void onPlayerRightClickEmpty(ItemStack stack, PlayerInteractEvent.RightClickEmpty event) {
		handleRightClickEvent(stack, event, null);
	}

	@Override
	public void onPlayerRightClickBlock(ItemStack stack, PlayerInteractEvent.RightClickBlock event) {
		BlockState block = event.getLevel().getBlockState(event.getHitVec().getBlockPos());
		/*if (block.is(LLBlocks.LAYLINE_CHARGER.get())) {
			event.setUseBlock(Event.Result.ALLOW);
			return;
		}TODO recharge*/
		handleRightClickEvent(stack, event, null);
	}

	@Override
	public void onPlayerRightClickEntity(ItemStack stack, PlayerInteractEvent.EntityInteract event) {
		handleRightClickEvent(stack, event, toLiving(event.getTarget()));
	}

}
