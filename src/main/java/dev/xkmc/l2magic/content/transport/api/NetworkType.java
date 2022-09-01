package dev.xkmc.l2magic.content.transport.api;

public interface NetworkType {

	boolean testConsumption(int avail, int c);

	boolean shouldContinue(int available, int consumed, int size);

	int provide(int available, int consumed, int size);

}
