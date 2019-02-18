package spacemadness.com.lunarconsole.core;

import spacemadness.com.lunarconsole.concurrent.Dispatcher;

interface StreamSubscription<T> extends Disposable {
	StreamSubscription<T> onValue(OnValue<T> onValue);

	StreamSubscription<T> onError(OnError onError);

	StreamSubscription<T> onDone(OnDone onDone);

	StreamSubscription<T> listenOn(Dispatcher dispatcher);

	interface OnValue<T> {
		void onData(T t);
	}

	interface OnError {
		void onError(Exception e);
	}

	interface OnDone {
		void onDone();
	}
}
