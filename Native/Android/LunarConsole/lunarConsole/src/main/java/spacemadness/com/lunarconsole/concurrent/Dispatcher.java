package spacemadness.com.lunarconsole.concurrent;

public interface Dispatcher {
	void dispatch(Runnable r);
}
