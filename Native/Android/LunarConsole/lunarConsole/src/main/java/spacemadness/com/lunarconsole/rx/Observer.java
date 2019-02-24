package spacemadness.com.lunarconsole.rx;

/**
 * Provides a mechanism for receiving push-based notifications.
 */
public interface Observer<T> {
	/**
	 * Provides the observer with new data.
	 */
	void onNext(T value);

	/**
	 * Notifies the observer that the provider has experienced an error condition.
	 */
	void onError(Exception error);

	/**
	 * Notifies the observer that the provider has finished sending push-based notifications.
	 */
	void onCompleted();
}