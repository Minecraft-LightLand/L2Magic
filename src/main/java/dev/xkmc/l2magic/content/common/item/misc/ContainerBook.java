package dev.xkmc.l2magic.content.common.item.misc;

import dev.xkmc.l2library.repack.registrate.util.entry.MenuEntry;
import dev.xkmc.l2magic.init.data.LangData;
import dev.xkmc.l2magic.init.registrate.LLMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ContainerBook extends Item implements MenuProvider {

	private final Supplier<MenuEntry<?>> cont;

	public ContainerBook(Properties props, Supplier<MenuEntry<?>> cont) {
		super(props);
		this.cont = cont;
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide()) {
			player.openMenu(this);
		} else {
			player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0f, 1.0f);
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public Component getDisplayName() {
		return LangData.translate(LLMenu.getLangKey(cont.get().get()));
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory plInv, Player pl) {
		return cont.get().create(wid, plInv);
	}

	public interface IFac {

		AbstractContainerMenu create(int wid, Inventory plInv, Player pl);

	}

}
