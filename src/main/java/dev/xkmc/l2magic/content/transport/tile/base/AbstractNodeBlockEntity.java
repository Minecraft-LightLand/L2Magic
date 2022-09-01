package dev.xkmc.l2magic.content.transport.tile.base;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.api.NetworkType;
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

import java.util.HashMap;
import java.util.List;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends BaseBlockEntity
		implements TickableBlockEntity, IRenderableNode, ILinkableNode {

	public AbstractNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SerialClass.SerialField(toClient = true)
	private int cooldown;

	@SerialClass.SerialField(toClient = true)
	private final HashMap<BlockPos, CoolDownType> color = new HashMap<>();

	private boolean dirty = false;
	private int simulateCoolDown = 0;

	@Nullable
	private AABB compiledBox;

	public abstract int getMaxCoolDown();

	public abstract Connector getConnector();

	public int getMaxDistanceSqr() {
		return 256;
	}

	public final int getCoolDown() {
		return Math.min(getMaxCoolDown(), cooldown);
	}

	public final List<BlockPos> target() {
		return getConnector().target();
	}

	public final NetworkType getNetworkType() {
		return getConnector();
	}

	public final void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		if (simulate) simulateCoolDown = getMaxCoolDown();
		else cooldown = getMaxCoolDown();
		color.put(target, success ? CoolDownType.GREEN : CoolDownType.RED);
		dirty = true;
	}

	public final boolean isReady() {
		return cooldown == 0;
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(getBlockPos()))
			return;
		getConnector().link(pos);
		sync();
	}

	public void validate() {
		getConnector().removeIf(e -> {
			if (level == null) return true;
			BlockEntity be = level.getBlockEntity(e);
			if (be == null) return true;
			return be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().isEmpty();
		});
		sync();
	}

	public void removeAll() {
		getConnector().removeIf(e -> true);
		sync();
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && dirty) {
			if (cooldown < simulateCoolDown) {
				cooldown = simulateCoolDown;
				simulateCoolDown = 0;
			}
			sync();
			dirty = false;
		}
		if (cooldown > 0) {
			cooldown--;
			if (cooldown == 0) {
				color.clear();
			}
		}
	}

	@Override
	public AABB getRenderBoundingBox() {
		if (compiledBox == null) {
			compiledBox = new AABB(getBlockPos());
			for (BlockPos pos : target()) {
				compiledBox = compiledBox.minmax(new AABB(pos));
			}
		}
		return compiledBox;
	}

	public CoolDownType getType(BlockPos pos) {
		return color.getOrDefault(pos, CoolDownType.GREY);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		compiledBox = null;
	}

}
