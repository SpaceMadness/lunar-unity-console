package spacemadness.com.lunarconsole.core;

public interface StreamSubscriptionOwner<T> {
	void cancel(StreamSubscription<T> subscription);
}
