package spacemadness.com.lunarconsole.core;

/**
 * A simple callback that can receive from {@link LiveData}.
 *
 * @param <T> The type of the parameter
 * @see LiveData LiveData - for a usage description.
 */
public interface Observer<T> {
	/**
	 * Called when the data is changed.
	 * @param t The new data
	 */
	void onChanged(T t);
}