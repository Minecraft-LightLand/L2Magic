package dev.xkmc.l2magic.content.altar.tile;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.altar.methods.AltarPillarState;
import dev.xkmc.l2magic.content.altar.methods.DelayedTickerBlockEntity;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * On update, calculate distance to floor
 */
@SerialClass
public class PillarTopBlockEntity extends BaseBlockEntity implements DelayedTickerBlockEntity {

	@SerialClass.SerialField
	private boolean isGrounded;

	@SerialClass.SerialField
	private int floor;

	public PillarTopBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void update() {
		if (level == null || level.isClientSide()) return;
		boolean blockGrounded = getBlockState().getValue(AltarPillarState.GROUNDED);
		if (isGrounded != blockGrounded) {
			isGrounded = blockGrounded;
			floor = -1;
		}
		if (isGrounded) {
			floor = 1;
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			pos.set(getBlockPos());
			pos.move(Direction.DOWN);
			while (level.getBlockState(pos).is(LMBlocks.ALTAR_PILLAR.get())) {
				pos.move(Direction.DOWN);
				floor++;
			}
		}
	}

	public int getFloor() {
		if (!isGrounded) return -1;
		return floor;
	}

}
