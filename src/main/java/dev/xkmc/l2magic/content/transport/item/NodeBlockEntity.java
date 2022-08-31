package dev.xkmc.l2magic.content.transport.item;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
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

import java.util.List;

@SerialClass
public abstract class NodeBlockEntity<BE extends NodeBlockEntity<BE>> extends BaseBlockEntity {

	private final LazyOptional<NodalItemHandler> itemHandler = LazyOptional.of(() -> new NodalItemHandler(this));

	public NodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public abstract List<BlockPos> target();

	public abstract boolean isItemStackValid(ItemStack stack);

	public abstract NetworkType getNetworkType();

	public abstract void refreshCooldown(BlockPos target, boolean success);

	public abstract boolean isReady();

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

}
