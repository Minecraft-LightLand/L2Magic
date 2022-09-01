package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import dev.xkmc.l2magic.content.transport.connector.SimpleConnector;
import net.minecraft.core.BlockPos;
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
	private final SimpleConnector connector = new SimpleConnector();

	public RetrieverItemNodeBlockEntity(BlockEntityType<RetrieverItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public int getMaxCoolDown() {
		return 80;
	}

	@Override
	public Connector getConnector() {
		return connector;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide() && isReady()) {
			BlockPos next = getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
			BlockEntity target = level.getBlockEntity(next);
			if (target != null) {
				var lazyCap = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
			stack = target.extractItem(i, attempt.getCount(), false);
			getHandler().insertItem(0, stack, false);
			return;
		}
	}

}
