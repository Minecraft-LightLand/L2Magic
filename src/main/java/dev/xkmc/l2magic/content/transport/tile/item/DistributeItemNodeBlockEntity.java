package dev.xkmc.l2magic.content.transport.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import dev.xkmc.l2magic.content.transport.connector.ListConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class DistributeItemNodeBlockEntity extends AbstractItemNodeBlockEntity<DistributeItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final ListConnector connector = new ListConnector(NetworkType.FILL);

	public DistributeItemNodeBlockEntity(BlockEntityType<DistributeItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
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
