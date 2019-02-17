package spacemadness.com.lunarconsole.concurrent;

import spacemadness.com.lunarconsole.debug.Log;

import static spacemadness.com.lunarconsole.debug.Tags.CORE;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class AsyncPromise<T> implements Promise<T> {
	private final Dispatcher dispatcher;
	private OnFulfillCallback<T> resolveCallback;
	private OnRejectCallback rejectCallback;

	public AsyncPromise() {
		this(null);
	}

	public AsyncPromise(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public synchronized Promise<T> then(OnFulfillCallback<T> onValue) {
		this.resolveCallback = checkNotNull(onValue, "onValue");
		return this;
	}

	@Override
	public synchronized Promise<T> catchError(OnRejectCallback onError) {
		this.rejectCallback = checkNotNull(onError, "onError");
		return this;
	}

	public synchronized void resolve(final T value) {
		if (dispatcher != null) {
			dispatcher.dispatch(new Runnable() {
				@Override public void run() {
					resolveSync(value);
				}
			});
		} else {
			resolveSync(value);
		}
	}

	public synchronized void reject(final Exception e) {
		if (dispatcher != null) {
			dispatcher.dispatch(new Runnable() {
				@Override public void run() {
					rejectSync(e);
				}
			});
		} else {
			rejectSync(e);
		}
	}

	private void resolveSync(T value) {
		try {
			if (resolveCallback != null) {
				resolveCallback.onValue(value);
			}
		} catch (Exception e) {
			rejectSync(e);
		}
	}

	private void rejectSync(Exception e) {
		try {
			if (rejectCallback != null) {
				rejectCallback.onError(e);
			}
		} catch (Exception ex) {
			Log.e(CORE, "Exception while rejecting promise", ex);
		}
	}
}
