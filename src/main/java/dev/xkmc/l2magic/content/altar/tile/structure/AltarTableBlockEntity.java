package dev.xkmc.l2magic.content.altar.tile.structure;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public class AltarTableBlockEntity extends PillarTopBlockEntity {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	private BlockPos core;

	public AltarTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void update() {
		super.update();
		BlockPos newCore = updateCorePos();
		if (Objects.equals(core, newCore)) return;
		core = newCore;
		sync();
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
