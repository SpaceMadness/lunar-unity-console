package spacemadness.com.lunarconsole.core;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.debug.Log;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;
import static spacemadness.com.lunarconsole.utils.ThreadUtils.checkQueue;

public class LiveData<T> {
	private T value;
	private final DispatchQueue dispatchQueue;
	private final List<DisposableObserver> observers;

	public LiveData(T value) {
		this(value, DispatchQueue.mainQueue());
	}

	public LiveData(T value, DispatchQueue dispatchQueue) {
		this.value = value;
		this.dispatchQueue = checkNotNull(dispatchQueue, "dispatchQueue");
		this.observers = new ArrayList<>();
	}

	public T getValue() {
		return value;
	}

	protected void setValue(final T value) {
		this.value = value;

		dispatchQueue.dispatch(new Runnable() {
			@Override
			public void run() {
				notifyObservers(value);
			}
		});
	}

	//region Observers

	public Disposable observe(Observer<T> observer) {
		checkQueue(dispatchQueue);

		for (int i = 0; i < observers.size(); ++i) {
			final DisposableObserver subscriber = observers.get(i);
			if (subscriber.observer == observer) {
				return subscriber;
			}
		}

		final DisposableObserver subscriber = new DisposableObserver(observer);
		observers.add(subscriber);
		notifyObserver(observer, value);
		return subscriber;
	}

	private boolean removeObserver(Observer<T> observer) {
		checkQueue(dispatchQueue);

		for (int i = 0; i < observers.size(); ++i) {
			final DisposableObserver subscriber = observers.get(i);
			if (subscriber.observer == observer) {
				observers.remove(i);
				return true;
			}
		}
		return false;
	}

	private void notifyObservers(T value) {
		checkQueue(dispatchQueue);

		List<DisposableObserver> temp = new ArrayList<>(observers);
		for (int i = 0; i < temp.size(); ++i) {
			Observer<T> observer = temp.get(i).observer;
			notifyObserver(observer, value);
		}
	}

	private void notifyObserver(Observer<T> observer, T value) {
		checkQueue(dispatchQueue);

		try {
			observer.onChanged(value);
		} catch (Exception e) {
			Log.e(e, "Exception while notifying observer: " + observer);
		}
	}

	//endregion

	//region DisposableObserver

	private class DisposableObserver implements Disposable {
		private final Observer<T> observer;

		private DisposableObserver(Observer<T> observer) {
			if (observer == null) {
				throw new IllegalArgumentException("Observer is null");
			}
			this.observer = observer;
		}

		@Override
		public void dispose() {
			removeObserver(observer);
		}

		@Override public String toString() {
			return "DisposableObserver: " + observer;
		}
	}

	//endregion
}
