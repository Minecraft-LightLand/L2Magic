package dev.xkmc.l2magic.content.transport.api;

public interface NetworkType {

	boolean testConsumption(int c);

	boolean alwaysContinue();

	int provide(int available, int consumed, int size);

}
