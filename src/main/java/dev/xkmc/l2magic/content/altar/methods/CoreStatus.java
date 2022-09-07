package dev.xkmc.l2magic.content.altar.methods;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CoreStatus implements StringRepresentable {
	IDLE, READY, ACTIVATED, PROCESSING, ERROR;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean isReady() {
		return this == READY || isActivated();
	}

	public boolean isActivated() {
		return this == ACTIVATED || this == PROCESSING;
	}

	/**
	 * return the holder status representation.
	 * DARK means idle
	 * POWERED means ready
	 * CONNECTED means error
	 */
	public PillarStatus toHolderStatus() {
		return this == IDLE ? PillarStatus.DARK : this == READY ? PillarStatus.POWERED : PillarStatus.CONNECTED;
	}

}
