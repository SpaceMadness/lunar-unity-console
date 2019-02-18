package spacemadness.com.lunarconsole.core;

import spacemadness.com.lunarconsole.concurrent.Dispatcher;
import spacemadness.com.lunarconsole.core.StreamSubscription.OnValue;
import spacemadness.com.lunarconsole.core.StreamSubscription.OnDone;
import spacemadness.com.lunarconsole.core.StreamSubscription.OnError;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public abstract class Stream<T> implements StreamSubscriptionOwner<T> {
	private final SubscriptionList<T> subscriptions;
	private T value;

	protected Stream(T value) {
		this.value = value;
		subscriptions = new SubscriptionList<>(1);
	}

	//region Subscription

	public StreamSubscription<T> listen() {
		return listen(null, null, null, null);
	}

	public StreamSubscription<T> listen(OnValue<T> onValue) {
		return listen(null, onValue, null, null);
	}

	public StreamSubscription<T> listen(Dispatcher dispatcher, OnValue<T> onValue) {
		return listen(dispatcher, onValue, null, null);
	}

	public StreamSubscription<T> listen(OnValue<T> onValue, OnError onError) {
		return listen(null, onValue, onError, null);
	}

	public StreamSubscription<T> listen(OnValue<T> onValue, OnError onError, OnDone onDone) {
		return listen(null, onValue, onError, onDone);
	}

	public StreamSubscription<T> listen(Dispatcher dispatcher, OnValue<T> onValue, OnError onError, OnDone onDone) {
		StreamSubscriptionImpl<T> subscription = new StreamSubscriptionImpl<>(this);
		if (onValue != null) {
			subscription.onValue(onValue);
		}
		if (onError != null) {
			subscription.onError(onError);
		}
		if (onDone != null) {
			subscription.onDone(onDone);
		}
		if (dispatcher != null) {
			subscription.listenOn(dispatcher);
		}
		register(subscription);
		return subscription;
	}

	protected void addValue(T value) {
		this.value = value;
		subscriptions.notifyValue(value);
	}

	protected void addError(Exception error) {
		subscriptions.notifyError(error);
	}

	protected void close() {
		subscriptions.notifyDone();
		subscriptions.clear();
	}

	@Override
	public void cancel(StreamSubscription<T> subscription) {
		unregister(checkNotNull(subscription, "subscription"));
	}

	private void register(StreamSubscriptionImpl<T> subscription) {
		subscriptions.register(subscription);
		subscription.onValue(value);
	}

	private void unregister(StreamSubscription<T> subscription) {
		subscriptions.unregister(subscription);
	}

	//endregion
}
