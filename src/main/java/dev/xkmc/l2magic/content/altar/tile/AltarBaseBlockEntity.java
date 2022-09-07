package dev.xkmc.l2magic.content.altar.tile;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.altar.methods.AltarBaseState;
import dev.xkmc.l2magic.content.altar.methods.DelayedTickerBlockEntity;
import dev.xkmc.l2magic.init.registrate.LMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

@SerialClass
public class AltarBaseBlockEntity extends BaseBlockEntity implements DelayedTickerBlockEntity {

	@SerialClass.SerialField
	private int dx, dz, height;

	@SerialClass.SerialField
	private boolean wasCore, wasValid;

	public AltarBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void update() {
		if (level == null || level.isClientSide()) return;
		boolean isCore = getBlockState().getValue(AltarBaseState.DISTANCE) == 0;
		if (isCore) {
			wasCore = isCore;
			return;
		}
		wasCore = false;
		height = 0;
		boolean isValid = AltarBaseState.isPowered(getBlockState());
		if (isValid) {
			wasValid = true;
			for (int i = 0; i < 4; i++) {
				Direction dire = Direction.from2DDataValue(i);
				BlockPos pos = getBlockPos().relative(dire);
				if (level.getBlockEntity(pos) instanceof AltarBaseBlockEntity base) {
					if (base.wasValid && AltarBaseState.isPowered(base.getBlockState())) {
						dx = base.dx - dire.getStepX();
						dz = base.dz - dire.getStepX();
						break;
					}
				}
			}
			return;
		}
		wasValid = false;
		dx = 0;
		dz = 0;
	}

	@Override
	public void blockRemoved() {
		if (level != null && !level.isClientSide() && wasCore) {
			BlockPos center = getBlockPos().above(height);
			if (level.getBlockEntity(center) instanceof AltarCoreBlockEntity core) {
				core.removeCenter();
			}
		}
		clearAll();
	}

	protected void clearAll() {
		if (level != null && !level.isClientSide() && wasCore) {
			BlockPos pos = getBlockPos();
			Set<BlockPos> set = new LinkedHashSet<>();
			Queue<BlockPos> queue = new ArrayDeque<>();
			queue.add(pos);
			set.add(pos);
			while (queue.size() > 0) {
				BlockPos currentPos = queue.poll();
				BlockState currentState = currentPos.equals(getBlockPos()) ? getBlockState() : level.getBlockState(currentPos);
				int dis = currentState.getValue(AltarBaseState.DISTANCE);
				for (int i = 0; i < 4; i++) {
					Direction dire = Direction.from2DDataValue(i);
					BlockPos nextPos = currentPos.relative(dire);
					BlockState nextState = level.getBlockState(nextPos);
					if (!set.contains(nextPos) &&
							nextState.is(LMBlocks.ALTAR_BASE.get()) &&
							nextState.getValue(AltarBaseState.DISTANCE) > dis) {
						queue.add(nextPos);
					}
				}
			}
			BlockState zero = LMBlocks.ALTAR_BASE.getDefaultState();
			if (level.getBlockState(getBlockPos()) != getBlockState()) set.remove(getBlockPos());
			for (BlockPos iPos : set) {
				level.setBlock(iPos, zero, 2);
			}
			StructureDebugHandler.info("set base block to dark");
		}
	}

	public void markActivated(int height) {
		this.wasCore = true;
		this.height = height;
	}

	@Nullable
	public AltarCoreBlockEntity getCenter() {
		if (level == null || level.isClientSide()) return null;
		if (wasCore) {
			BlockPos core = getBlockPos().above(height);
			if (level.getBlockEntity(core) instanceof AltarCoreBlockEntity ans) {
				return ans;
			}
			return null;
		}
		if (!wasValid || dx == 0 && dz == 0) return null;
		BlockPos center = getBlockPos().offset(dx, 0, dz);
		if (level.getBlockEntity(center) instanceof AltarBaseBlockEntity entity &&
				entity.wasCore) {
			return entity.getCenter();
		}
		return null;
	}

}
