package dev.xkmc.l2magic.content.altar.methods;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum PillarStatus implements StringRepresentable {
	DARK(false, false),
	CONNECTED(true, false),
	POWERED(false, true);

	private final boolean connected, powered;

	PillarStatus(boolean connected, boolean powered) {
		this.connected = connected;
		this.powered = powered;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean connectsTable() {
		return connected;
	}

	public boolean connectsHolder() {
		return powered;
	}

	public int lighted(boolean grounded) {
		return powered ? grounded ? 15 : 7 : connected ? grounded ? 7 : 0 : 0;
	}

	public PillarStatus getDefault() {
		if (connectsHolder()) return DARK;
		return this;
	}

}
