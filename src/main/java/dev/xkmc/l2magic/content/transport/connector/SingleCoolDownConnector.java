package dev.xkmc.l2magic.content.transport.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2magic.content.transport.tile.base.CoolDownType;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.List;

@SerialClass
public abstract class SingleCoolDownConnector implements Connector {

	@SerialClass.SerialField(toClient = true)
	private int coolDown;

	@SerialClass.SerialField(toClient = true)
	private final HashMap<BlockPos, CoolDownType> color = new HashMap<>();

	private int simulatedCoolDown;

	public int maxCoolDown;

	@Override
	public List<BlockPos> getAvailableTarget() {
		return getConnected();
	}

	protected SingleCoolDownConnector(int maxCoolDown) {
		this.maxCoolDown = maxCoolDown;
	}

	@Override
	public int getMaxCoolDown(BlockPos pos) {
		return maxCoolDown;
	}

	@Override
	public int getCoolDown(BlockPos pos) {
		return Math.min(getMaxCoolDown(pos), coolDown);
	}

	@Override
	public void perform() {
		if (coolDown < simulatedCoolDown) {
			coolDown = simulatedCoolDown;
			simulatedCoolDown = 0;
		}
	}

	@Override
	public void tick() {
		if (coolDown > 0) {
			coolDown--;
			if (coolDown == 0) {
				color.clear();
			}
		}
	}

	@Override
	public void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		if (simulate) simulatedCoolDown = getMaxCoolDown(target);
		else coolDown = getMaxCoolDown(target);
		color.put(target, success ? CoolDownType.GREEN : CoolDownType.RED);
	}

	public CoolDownType getType(BlockPos pos) {
		return color.getOrDefault(pos, CoolDownType.GREY);
	}

	@Override
	public boolean isReady() {
		return coolDown == 0;
	}

}
