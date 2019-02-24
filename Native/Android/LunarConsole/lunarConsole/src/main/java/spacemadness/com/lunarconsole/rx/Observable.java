package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

/**
 * Defines a provider for push-based notification.
 */
public abstract class Observable<T> {
	abstract Disposable subscribe(Observer<T> observer);
}
