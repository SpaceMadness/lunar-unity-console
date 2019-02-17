package spacemadness.com.lunarconsole.concurrent;

/**
 * Represents the eventual completion (or failure) of an asynchronous operation, and its resulting value.
 */
public interface Promise<T> {
	/**
	 * Appends fulfillment handlers to the promise, and returns a new promise resolving to the return value
	 * of the called handler, or to its original settled value if the promise was not handled.
	 */
	Promise<T> then(OnFulfillCallback<T> onValue);

	/**
	 * Appends a rejection handler callback to the promise, and returns a new promise resolving to the return value
	 * of the callback if it is called, or to its original fulfillment value if the promise is instead fulfilled.
	 */
	Promise<T> catchError(OnRejectCallback onError);

	/**
	 * Represents a fulfilment callback.
	 */
	interface OnFulfillCallback<T> {
		void onValue(T value);
	}

	/**
	 * Represents a rejection callback.
	 */
	interface OnRejectCallback {
		void onError(Exception e);
	}
}