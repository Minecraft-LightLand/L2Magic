package dev.xkmc.l2magic.content.altar.tile;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.altar.methods.AltarPillarState;
import dev.xkmc.l2magic.content.altar.methods.DelayedTicker;
import dev.xkmc.l2magic.content.altar.methods.PillarStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class AltarHolderBlockEntity extends PillarTopBlockEntity {

	@SerialClass.SerialField
	protected boolean hasTarget, hasError;

	public AltarHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void update() {
		super.update();
		notifyHolder();
	}

	public void blockRemoved() {
		notifyHolder();
	}

	private void notifyHolder() {
		if (level == null || level.isClientSide()) return;
		int count = 0;
		for (int i = 0; i < 4; i++) {
			BlockPos pos = getBlockPos()
					.relative(Direction.from2DDataValue(i))
					.relative(Direction.from2DDataValue(i + 1))
					.relative(Direction.UP);
			if (level.getBlockEntity(pos) instanceof AltarCoreBlockEntity core) {
				count++;
				core.onHolderUpdate();
			}
		}
		hasTarget = count == 1;
		hasError = count > 1;
		if (hasError) {
			BlockState newState = getBlockState().setValue(AltarPillarState.PILLAR, PillarStatus.CONNECTED);
			if (newState != getBlockState()) {
				level.setBlockAndUpdate(getBlockPos(), newState);
				StructureDebugHandler.info("set holder to " + newState + " at " + getBlockPos());
			}
		}
	}

	public void refreshState() {
		if (level == null || level.isClientSide()) return;
		if (getBlockState().getValue(AltarPillarState.PILLAR) != PillarStatus.DARK) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(AltarPillarState.PILLAR, PillarStatus.DARK));
			DelayedTicker.update(getBlockState().getBlock(), level, getBlockPos());
			StructureDebugHandler.info("set holder to DARK at " + getBlockPos());
		}
	}

}
