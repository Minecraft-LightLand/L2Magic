package dev.xkmc.l2magic.content.transport.api;

import dev.xkmc.l2magic.init.L2Magic;

/**
 * Used to guard item usage, to make sure that no item dupe happens
 */
public class RealToken<T> {

	private final IContentHolder<T> holder;
	private int count;

	public RealToken(IContentHolder<T> holder, int count) {
		this.holder = holder;
		this.count = count;
	}

	public T split(int consumed) {
		if (consumed > count) {
			L2Magic.LOGGER.error("Consumes more than available. Consumed: " + consumed + ", Available: " + count + ", Content: " + holder.get());
		}
		int val = Math.min(consumed, count);
		count -= val;
		return holder.getCopy(val);
	}

	public void gain(int remain) {
		count += remain;
	}

	public T getRemain() {
		return holder.getCopy(count);
	}

}
