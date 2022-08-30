package dev.xkmc.l2magic.content.transport.api;

/**
 * Holder of content, such as ItemStack, FluidStack, Energy, Gas.
 * This should be immutable.
 */
public interface IContentHolder<T> {

	/**
	 * returns the original amount.
	 */
	int getCount();

	/**
	 * returns the original stack. Should not be modified
	 */
	T get();

	/**
	 * returns a copy of the original stack.
	 */
	T getCopy(int count);

	/**
	 * returns the empty representation of the content type
	 */
	T empty();

	/**
	 * returns a guarded stack
	 */
	default RealToken<T> toReal() {
		return new RealToken<>(this, getCount());
	}

}
