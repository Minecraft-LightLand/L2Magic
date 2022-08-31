package dev.xkmc.l2magic.content.transport.api;

/**
 * Token of content,
 */
public interface IContentToken<T> {

	/**
	 * get the immutable content representing the original stack
	 */
	IContentHolder<T> get();

	/**
	 * get the remaining available content
	 */
	int getAvailable();

	/**
	 * deduct content from available. Can deduct more than available
	 */
	void consume(int count);

	/**
	 * get the remaining content
	 */
	default T getRemain() {
		return get().getCopy(getAvailable());
	}

}
