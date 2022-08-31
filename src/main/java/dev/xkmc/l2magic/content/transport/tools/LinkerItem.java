package dev.xkmc.l2magic.content.transport.tools;

import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.content.transport.tile.ILinkableNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;

public class LinkerItem extends Item {

	private static final String KEY_POS = "first_pos";

	public LinkerItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		ItemStack stack = ctx.getItemInHand();
		BlockEntity old = null;
		if (stack.getTag() != null && stack.getTag().contains(KEY_POS, Tag.TAG_COMPOUND)) {
			BlockPos pos = new NBTObj(stack, KEY_POS).toBlockPos();
			old = ctx.getLevel().getBlockEntity(pos);
		}
		if (old instanceof ILinkableNode node) {
			if (be != null) {
				var lazyCap = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
				if (lazyCap.resolve().isPresent()) {
					if (!ctx.getLevel().isClientSide()) {
						node.link(ctx.getClickedPos());
						stack.removeTagKey(KEY_POS);
					}
					return InteractionResult.SUCCESS;
				}
			}
		} else if (be instanceof ILinkableNode) {
			if (!ctx.getLevel().isClientSide()) {
				new NBTObj(stack, KEY_POS).fromBlockPos(ctx.getClickedPos());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
