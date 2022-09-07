package dev.xkmc.l2magic.content.altar.methods;

public interface DelayedTickerBlockEntity {

	void update();

	default void blockRemoved(){}

}
