package dev.xkmc.l2magic.content.transport.tools;

import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2magic.content.transport.tile.base.ILinkableNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;

public class LinkerItem extends Item implements ILinker {

	private static final String KEY_POS = "first_pos";

	@Nullable
	public static BlockPos getPos(ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains(KEY_POS, Tag.TAG_COMPOUND)) {
			return new NBTObj(stack, KEY_POS).toBlockPos();
		}
		return null;
	}

	public LinkerItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		ItemStack stack = ctx.getItemInHand();
		BlockEntity old = null;
		BlockPos storedPos = getPos(stack);
		if (storedPos != null) {
			old = ctx.getLevel().getBlockEntity(storedPos);
		}
		if (old instanceof ILinkableNode node) {
			if (old.getBlockPos().distSqr(ctx.getClickedPos()) > node.getMaxDistanceSqr()) {
				old = null;
			}
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
		}
		if (be instanceof ILinkableNode) {
			if (!ctx.getLevel().isClientSide()) {
				new NBTObj(stack, KEY_POS).fromBlockPos(ctx.getClickedPos());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean storesPos() {
		return true;
	}

}
