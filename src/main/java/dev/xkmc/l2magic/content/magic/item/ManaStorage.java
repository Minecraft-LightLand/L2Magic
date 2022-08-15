package dev.xkmc.l2magic.content.magic.item;

import dev.xkmc.l2magic.content.common.capability.MagicData;
import dev.xkmc.l2magic.network.packets.CapToClient;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaStorage extends Item {

	public static final int ARCANE_COST = 16;

	public final Item container;
	public final int mana;

	public ManaStorage(Properties props, Item container, int mana) {
		super(props);
		this.container = container;
		this.mana = mana;
	}

	public ItemStack finishUsingItem(ItemStack stack, Level w, LivingEntity e) {
		if (e instanceof ServerPlayer sp) {
			if (stack.isEdible()) {
				MagicData data = MagicData.get(sp);
				data.magicAbility.giveMana(mana);
				data.magicAbility.addSpellLoad(-mana);
				new CapToClient(CapToClient.Action.MAGIC_ABILITY, data).toClientPlayer(sp);
			}
		}
		return super.finishUsingItem(stack, w, e);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
