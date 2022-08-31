package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import dev.xkmc.l2magic.content.transport.connector.OrderedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class OrderedItemNodeBlockEntity extends AbstractItemNodeBlockEntity<OrderedItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final OrderedConnector connector = new OrderedConnector(this);

	public OrderedItemNodeBlockEntity(BlockEntityType<OrderedItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
}
