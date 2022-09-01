package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.item.ItemNodeEntity;
import dev.xkmc.l2magic.content.transport.item.NodalItemHandler;
import dev.xkmc.l2magic.content.transport.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2magic.content.transport.tile.base.IRenderableItemNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractItemNodeBlockEntity<BE extends AbstractItemNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements ItemNodeEntity, IRenderableItemNode {

	protected final LazyOptional<NodalItemHandler> itemHandler = LazyOptional.of(() -> new NodalItemHandler(this));

	public AbstractItemNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SerialClass.SerialField(toClient = true)
	public ItemStack filter = ItemStack.EMPTY;

	public ItemStack getItem() {
		return filter;
	}

	public final boolean isItemStackValid(ItemStack stack) {
		if (filter.isEmpty()) {
			return true;
		}
		return stack.getItem() == filter.getItem();
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	protected NodalItemHandler getHandler() {
		return itemHandler.resolve().get();
	}

}
