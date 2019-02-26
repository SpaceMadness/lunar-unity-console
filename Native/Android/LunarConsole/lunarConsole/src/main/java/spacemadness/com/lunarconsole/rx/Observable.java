package spacemadness.com.lunarconsole.rx;

import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.console.ConsoleLogEntry;
import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

/**
 * Defines a provider for push-based notification.
 */
public abstract class Observable<T> {
	public abstract Disposable subscribe(Observer<T> observer);

	public Observable<List<ConsoleLogEntry>> subscribeOn(DispatchQueue queue) {
		throw new NotImplementedException();
	}
}
