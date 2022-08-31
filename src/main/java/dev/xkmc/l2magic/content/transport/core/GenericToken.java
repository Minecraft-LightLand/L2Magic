package dev.xkmc.l2magic.content.transport.core;

import dev.xkmc.l2magic.content.transport.api.IContentHolder;
import dev.xkmc.l2magic.content.transport.api.IContentToken;

public class GenericToken<T> implements IContentToken<T> {

	private final IContentHolder<T> holder;
	private int count;

	public GenericToken(IContentHolder<T> holder) {
		this.holder = holder;
		this.count = holder.getCount();
	}

	@Override
	public IContentHolder<T> get() {
		return holder;
	}

	@Override
	public int getAvailable() {
		return count;
	}

	@Override
	public void consume(int val) {
		count -= val;
	}

}
