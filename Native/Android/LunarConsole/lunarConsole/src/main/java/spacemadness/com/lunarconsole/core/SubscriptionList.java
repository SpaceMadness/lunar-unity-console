package spacemadness.com.lunarconsole.core;

import java.util.ArrayList;
import java.util.List;

class SubscriptionList<T> {
	private final List<StreamSubscriptionImpl<T>> subscriptions;

	public SubscriptionList(int capacity) {
		subscriptions = new ArrayList<>(capacity);
	}

	public synchronized void register(StreamSubscriptionImpl<T> subscription) {
		subscriptions.add(subscription);
	}

	public synchronized void unregister(StreamSubscription<T> subscription) {
		//noinspection SuspiciousMethodCalls
		subscriptions.remove(subscription);
	}

	public synchronized void clear() {
		subscriptions.clear();
	}

	public synchronized void notifyValue(final T value) {
		List<StreamSubscriptionImpl<T>> temp = new ArrayList<>(subscriptions);
		for (StreamSubscriptionImpl<T> subscription : temp) {
			subscription.onValue(value);
		}
	}

	public synchronized void notifyError(final Exception error) {
		List<StreamSubscriptionImpl<T>> temp = new ArrayList<>(subscriptions);
		for (StreamSubscriptionImpl<T> subscription : temp) {
			subscription.onError(error);
		}
	}

	public synchronized void notifyDone() {
		List<StreamSubscriptionImpl<T>> temp = new ArrayList<>(subscriptions);
		for (StreamSubscriptionImpl<T> subscription : temp) {
			subscription.onDone();
		}
	}
}
