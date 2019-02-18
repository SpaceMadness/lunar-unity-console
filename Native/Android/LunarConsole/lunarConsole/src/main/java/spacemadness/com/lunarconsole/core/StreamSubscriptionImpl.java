package spacemadness.com.lunarconsole.core;

import spacemadness.com.lunarconsole.concurrent.Dispatcher;
import spacemadness.com.lunarconsole.debug.Log;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public final class StreamSubscriptionImpl<T> implements StreamSubscription<T> {
	private final StreamSubscriptionOwner owner;
	private OnValue<T> onValueCallback;
	private OnError onErrorCallback;
	private OnDone onDoneCallback;
	private Dispatcher dispatcher;

	StreamSubscriptionImpl(StreamSubscriptionOwner owner) {
		this.owner = checkNotNull(owner, "owner");
	}

	@Override
	public synchronized StreamSubscription<T> onValue(OnValue<T> onValue) {
		this.onValueCallback = checkNotNull(onValue, "onValue");
		return this;
	}

	@Override
	public synchronized StreamSubscription<T> onError(OnError onError) {
		this.onErrorCallback = checkNotNull(onError, "onError");
		return this;
	}

	@Override
	public synchronized StreamSubscription<T> onDone(OnDone onDone) {
		this.onDoneCallback = checkNotNull(onDone, "onDone");
		return this;
	}

	@Override
	public synchronized StreamSubscription<T> listenOn(Dispatcher dispatcher) {
		this.dispatcher = checkNotNull(dispatcher, "dispatcher");
		return this;
	}

	//region Dispatch

	synchronized void onValue(final T t) {
		if (dispatcher != null) {
			dispatcher.dispatch(new Runnable() {
				@Override
				public void run() {
					onValueSync(t);
				}
			});
		} else {
			onValueSync(t);
		}
	}

	synchronized void onError(final Exception e) {
		if (dispatcher != null) {
			dispatcher.dispatch(new Runnable() {
				@Override
				public void run() {
					onErrorSync(e);
				}
			});
		} else {
			onErrorSync(e);
		}
	}

	synchronized void onDone() {
		if (dispatcher != null) {
			dispatcher.dispatch(new Runnable() {
				@Override
				public void run() {
					addDoneSync();
				}
			});
		} else {
			addDoneSync();
		}
	}

	private void onValueSync(T t) {
		try {
			if (onValueCallback != null) {
				onValueCallback.onData(t);
			}
		} catch (Exception e) {
			onErrorSync(e);
		}
	}

	private void onErrorSync(Exception e) {
		try {
			if (onErrorCallback != null) {
				onErrorCallback.onError(e);
			}
		} catch (Exception ex) {
			Log.e(ex, "Exception while notifying error to a stream subscription");
		}
	}

	private void addDoneSync() {
		try {
			if (onDoneCallback != null) {
				onDoneCallback.onDone();
			}
		} catch (Exception ex) {
			Log.e(ex, "Exception while notifying done to a stream subscription");
		}
	}

	//endregion

	//region Disposable

	@Override
	public synchronized void dispose() {
		//noinspection unchecked
		owner.cancel(this);
	}

	//endregion
}
