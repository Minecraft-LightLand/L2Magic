package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import dev.xkmc.l2magic.content.transport.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SerialClass
public class RetrieverItemNodeBlockEntity extends AbstractItemNodeBlockEntity<RetrieverItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final SimpleConnector connector = new SimpleConnector(80);

	public RetrieverItemNodeBlockEntity(BlockEntityType<RetrieverItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide() && getConnector().isReady()) {
			Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos next = getBlockPos().relative(facing);
			BlockEntity target = level.getBlockEntity(next);
			if (target != null) {
				var lazyCap = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					tryRetrieve(cap);
				}
			}

		}
		super.tick();
	}

	protected void tryRetrieve(IItemHandler target) {
		for (int i = 0; i < target.getSlots(); i++) {
			ItemStack stack = target.extractItem(i, 64, true);
			if (stack.isEmpty()) continue;
			ItemStack attempt = getHandler().insertItem(0, stack, true);
			if (attempt.getCount() == stack.getCount()) continue;
			stack = target.extractItem(i, stack.getCount() - attempt.getCount(), false);
			ItemStack leftover = getHandler().insertItem(0, stack, false);
			drop(leftover);
			return;
		}
	}

	private void drop(ItemStack stack) {
		if (stack.isEmpty()) return;
		if (level == null || level.isClientSide()) return;
		BlockPos pos = getBlockPos();
		level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack));
	}

}
