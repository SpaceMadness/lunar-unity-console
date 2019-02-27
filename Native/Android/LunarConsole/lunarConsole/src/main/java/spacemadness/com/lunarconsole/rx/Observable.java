package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.concurrent.Dispatcher;
import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

/**
 * Defines a provider for push-based notification.
 */
public abstract class Observable<T> {
	public abstract Disposable subscribe(Observer<T> observer);

	public Observable<T> observeOn(Dispatcher dispatcher) {
		throw new NotImplementedException();
	}
}
