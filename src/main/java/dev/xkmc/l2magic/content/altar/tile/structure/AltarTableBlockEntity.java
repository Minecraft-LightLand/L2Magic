package dev.xkmc.l2magic.content.altar.tile.structure;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.altar.methods.AltarCoreState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public class AltarTableBlockEntity extends PillarTopBlockEntity implements TickableBlockEntity {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	private BlockPos core;

	public AltarTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {

	}

	// core reference code

	@Override
	public void update() {
		super.update();
		BlockPos newCore = updateCorePos();
		if (Objects.equals(core, newCore)) return;
		if (core != null) updateCore(core, false);
		core = newCore;
		if (core != null) updateCore(core, true);
		sync();
	}

	@Override
	public void blockRemoved() {
		if (core != null) updateCore(core, false);
	}

	private void updateCore(BlockPos corePos, boolean add) {
		if (level == null || level.isClientSide()) return;
		if (level.getBlockEntity(corePos) instanceof AltarCoreBlockEntity coreEntity) {
			if (coreEntity.getBlockState().getValue(AltarCoreState.STATUS).isActivated()) {
				coreEntity.getManager().addTable(getBlockPos(), add);
			}
		}
	}

	@Nullable
	public BlockPos getCorePos() {
		if (level == null || getFloor() <= 0) {
			return null;
		}
		return core;
	}

	@Nullable
	private BlockPos updateCorePos() {
		if (level != null && !level.isClientSide() && getFloor() > 0) {
			BlockPos base = getBlockPos().below(getFloor());
			if (level.getBlockEntity(base) instanceof AltarBaseBlockEntity baseEntity) {
				AltarCoreBlockEntity core = baseEntity.getCenter();
				if (core != null) {
					return core.getBlockPos();
				}
			}
		}
		return null;
	}

	// client render code

	@Nullable
	private AABB compiledBox;

	@Override
	public AABB getRenderBoundingBox() {
		if (compiledBox == null) {
			compiledBox = new AABB(getBlockPos());
			BlockPos target = getCorePos();
			if (target != null)
				compiledBox = compiledBox.minmax(new AABB(target));
		}
		return compiledBox;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		compiledBox = null;
	}

}
