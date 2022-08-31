package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import dev.xkmc.l2magic.content.transport.item.ItemNodeEntity;
import dev.xkmc.l2magic.content.transport.item.NodalItemHandler;
import dev.xkmc.l2magic.content.transport.tile.CoolDownType;
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

import java.util.HashMap;
import java.util.List;

@SerialClass
public abstract class AbstractItemNodeBlockEntity<BE extends AbstractItemNodeBlockEntity<BE>> extends BaseBlockEntity
		implements ItemNodeEntity, TickableBlockEntity {

	private final LazyOptional<NodalItemHandler> itemHandler = LazyOptional.of(() -> new NodalItemHandler(this));

	public AbstractItemNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SerialClass.SerialField(toClient = true)
	protected int cooldown;

	@SerialClass.SerialField(toClient = true)
	protected HashMap<BlockPos, CoolDownType> color = new HashMap<>();

	@SerialClass.SerialField(toClient = true)
	public ItemStack filter = ItemStack.EMPTY;

	private boolean dirty = false;

	public abstract int getMaxCoolDown();

	public abstract Connector getConnector();

	public final List<BlockPos> target() {
		return getConnector().target();
	}

	public final NetworkType getNetworkType() {
		return getConnector().getNetworkType();
	}

	public final boolean isItemStackValid(ItemStack stack) {
		if (filter.isEmpty()) {
			return true;
		}
		return stack.getItem() == filter.getItem();
	}

	public final void refreshCoolDown(BlockPos target, boolean success) {
		cooldown = getMaxCoolDown();
		color.put(target, success ? CoolDownType.GREEN : CoolDownType.RED);
		dirty = true;
	}

	public boolean isReady() {
		return cooldown == 0;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && dirty) {
			sync();
			dirty = false;
		}
		if (cooldown > 0) {
			cooldown--;
			if (cooldown == 0) {
				color.clear();
			}
		}
	}

	public CoolDownType getType(BlockPos pos) {
		return color.getOrDefault(pos, CoolDownType.GREY);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

}
