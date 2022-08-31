package dev.xkmc.l2magic.content.transport.api;

public enum NetworkType {
	ONE, SYNCED, FILL;

	public boolean testConsumption(int c) {
		return this != SYNCED || c == 1;
	}

	public boolean alwaysContinue() {
		return this == SYNCED;
	}

	public int provide(int available, int consumed, int size) {
		if (this == SYNCED) return 1;
		if (this == FILL) return Math.max(available - consumed, available / size);
		return Math.max(0, available - consumed);
	}
}
