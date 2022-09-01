package dev.xkmc.l2magic.content.transport.tile.base;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends BaseBlockEntity
		implements TickableBlockEntity, IRenderableNode, ILinkableNode {

	public AbstractNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private boolean dirty = false;

	@Nullable
	private AABB compiledBox;

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && dirty) {
			getConnector().perform();
			sync();
			dirty = false;
		}
		getConnector().tick();
	}

	public abstract Connector getConnector();

	public final void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		getConnector().refreshCoolDown(target, success, simulate);
		dirty = true;
	}

	// linkable

	@Override
	public int getMaxDistanceSqr() {
		return 256;
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(getBlockPos()))
			return;
		getConnector().link(pos);
		sync();
	}

	@Override
	public void validate() {
		getConnector().removeIf(e -> {
			if (level == null) return true;
			BlockEntity be = level.getBlockEntity(e);
			if (be == null) return true;
			return be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().isEmpty();
		});
		sync();
	}

	@Override
	public void removeAll() {
		getConnector().removeIf(e -> true);
		sync();
	}

	// render related

	@Override
	public AABB getRenderBoundingBox() {
		if (compiledBox == null) {
			compiledBox = new AABB(getBlockPos());
			for (BlockPos pos : getConnector().target()) {
				compiledBox = compiledBox.minmax(new AABB(pos));
			}
		}
		return compiledBox;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		compiledBox = null;
	}

}
