package dev.xkmc.l2magic.content.altar.tile.structure;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.altar.methods.*;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class AltarCoreBlockEntity extends BaseBlockEntity implements TickableBlockEntity, ActivatableBlockEntity, DelayedTickerBlockEntity {

	@SerialClass.SerialField
	private boolean holderDirty, centerDirty;

	@SerialClass.SerialField
	private int height;

	public AltarCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide()) return;
		CoreStatus oldStatus = getBlockState().getValue(AltarCoreState.STATUS);
		CoreStatus newStatus = oldStatus;
		if (holderDirty) {
			newStatus = holderCleanUp();
			if (newStatus != oldStatus) {
				setupHolders(newStatus);
				if (oldStatus.isActivated())
					centerDirty = true;
			}
		}
		if (centerDirty) {
			int oldHeight = height;
			newStatus = centerCleanUp();
			if (newStatus != oldStatus) {
				setupCenter(newStatus, newStatus.isActivated() ? height : oldHeight);
			}
		}
		if (newStatus != oldStatus) {
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(AltarCoreState.STATUS, newStatus));
			StructureDebugHandler.info("setup core to " + newStatus + " at " + getBlockPos());
		}
	}

	public void onHolderUpdate() {
		holderDirty = true;
	}

	private CoreStatus holderCleanUp() {
		holderDirty = false;
		if (level == null || level.isClientSide()) return CoreStatus.ERROR;
		boolean isIdle = false;
		for (int i = 0; i < 4; i++) {
			BlockPos pos = getBlockPos()
					.relative(Direction.from2DDataValue(i))
					.relative(Direction.from2DDataValue(i + 1))
					.relative(Direction.DOWN);
			BlockState state = level.getBlockState(pos);
			if (!state.is(LMBlocks.ALTAR_HOLDER.get()) ||
					!state.getValue(AltarPillarState.GROUNDED) ||
					state.getValue(AltarPillarState.PILLAR).connectsTable()) {
				isIdle = true;
				continue;
			}
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof AltarHolderBlockEntity holder)) return CoreStatus.ERROR;
			if (holder.hasError || !holder.hasTarget) return CoreStatus.ERROR;
		}
		return isIdle ? CoreStatus.IDLE : CoreStatus.READY;
	}

	private void setupHolders(CoreStatus status) {
		if (level == null || level.isClientSide()) return;
		for (int i = 0; i < 4; i++) {
			BlockPos pos = getBlockPos()
					.relative(Direction.from2DDataValue(i))
					.relative(Direction.from2DDataValue(i + 1))
					.relative(Direction.DOWN);
			BlockState state = level.getBlockState(pos);
			if (!state.is(LMBlocks.ALTAR_HOLDER.get())) continue;
			if (state.getValue(AltarPillarState.PILLAR).connectsTable()) continue; // Holder is in error state;
			BlockState newState = state.setValue(AltarPillarState.PILLAR, status.toHolderStatus());
			if (newState != state) {
				// activate pillar
				level.setBlockAndUpdate(pos, newState);
				StructureDebugHandler.info("set holder to " + newState + " at " + pos);
			}
		}
	}

	private CoreStatus centerCleanUp() {
		centerDirty = false;
		height = -1;
		if (level == null || level.isClientSide()) return CoreStatus.ERROR;
		if (!getBlockState().getValue(AltarCoreState.STATUS).isReady()) return CoreStatus.IDLE;
		int y = 0;
		for (int i = 0; i < 4; i++) {
			BlockPos pos = getBlockPos()
					.relative(Direction.from2DDataValue(i))
					.relative(Direction.from2DDataValue(i + 1))
					.relative(Direction.DOWN);
			BlockState state = level.getBlockState(pos);
			if (!state.is(LMBlocks.ALTAR_HOLDER.get())) return CoreStatus.IDLE;
			if (!state.getValue(AltarPillarState.PILLAR).connectsHolder()) return CoreStatus.IDLE;
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof AltarHolderBlockEntity holder)) return CoreStatus.ERROR;
			int hy = holder.getFloor();
			if (hy <= 0) return CoreStatus.READY;
			if (y == 0) y = hy;
			else if (y != hy) return CoreStatus.READY;
		}
		BlockPos core = getBlockPos().below(y + 1);
		BlockState state = level.getBlockState(core);
		if (!state.is(LMBlocks.ALTAR_BASE.get())) return CoreStatus.READY;
		BlockEntity coreBE = level.getBlockEntity(core);
		if (!(coreBE instanceof AltarBaseBlockEntity base)) return CoreStatus.READY;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0) continue;
				if (!level.getBlockState(core.offset(x, 0, z)).is(LMBlocks.ALTAR_BASE.get()))
					return CoreStatus.READY;
			}
		height = y + 1;
		return CoreStatus.ACTIVATED;
	}

	private void setupCenter(CoreStatus status, int height) {
		if (level == null || level.isClientSide()) return;
		BlockPos pos = getBlockPos().below(height);
		BlockState state = level.getBlockState(pos);
		if (status.isActivated()) {
			BlockState newState = state.setValue(AltarBaseState.DISTANCE, 0);
			if (newState != state) {
				level.setBlockAndUpdate(pos, newState);
				StructureDebugHandler.info("set core block to " + newState + " at " + pos);
				if (level.getBlockEntity(pos) instanceof AltarBaseBlockEntity center) {
					center.markActivated(height);
				}
			}
		} else if (level.getBlockEntity(pos) instanceof AltarBaseBlockEntity base &&
				base.getBlockState().getValue(AltarBaseState.DISTANCE) == 0) {
			base.clearAll();
		}
	}

	@Override
	public boolean activate(ItemStack stack) {
		if (getBlockState().getValue(AltarCoreState.STATUS) == CoreStatus.READY) {
			centerDirty = true;
			return true;
		}
		return false;
	}

	public void removeCenter() {
		centerDirty = true;
	}

	@Override
	public void update() {

	}

	@Override
	public void blockRemoved() {
		if (level == null || level.isClientSide()) return;
		for (int i = 0; i < 4; i++) {
			BlockPos pos = getBlockPos()
					.relative(Direction.from2DDataValue(i))
					.relative(Direction.from2DDataValue(i + 1))
					.relative(Direction.DOWN);
			if (level.getBlockEntity(pos) instanceof AltarHolderBlockEntity holder) {
				holder.refreshState();
			}
		}
	}

}
