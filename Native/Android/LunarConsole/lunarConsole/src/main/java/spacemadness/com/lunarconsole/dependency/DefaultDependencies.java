package spacemadness.com.lunarconsole.dependency;

import android.os.Looper;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;
import spacemadness.com.lunarconsole.concurrent.SerialDispatchQueue;

public final class DefaultDependencies {
	public static void register() {
		Provider.register(DispatchQueueProvider.class, createDispatchQueueProvider());
	}

	private static DispatchQueueProvider createDispatchQueueProvider() {
		return new DispatchQueueProvider() {
			@Override public DispatchQueue createMainQueue() {
				return new SerialDispatchQueue(Looper.getMainLooper(), "main");
			}

			@Override public DispatchQueue createSerialQueue(String name) {
				return new SerialDispatchQueue(name);
			}

			@Override public boolean isMainQueue() {
				return Looper.getMainLooper() == Looper.myLooper();
			}
		};
	}
}
