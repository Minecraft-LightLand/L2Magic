package dev.xkmc.l2magic.content.common.item.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ScreenBook extends Item {

	public Supplier<Supplier<?>> sup;

	public ScreenBook(Properties props, Supplier<Supplier<?>> sup) {
		super(props.stacksTo(1));
		this.sup = sup;
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (world.isClientSide()) {
			player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0f, 1.0f);
			Minecraft.getInstance().setScreen((Screen) sup.get().get());
		}
		return InteractionResultHolder.success(stack);
	}

}
