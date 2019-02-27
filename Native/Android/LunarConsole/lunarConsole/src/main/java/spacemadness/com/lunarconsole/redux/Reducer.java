package spacemadness.com.lunarconsole.redux;

public interface Reducer<T extends State> {
	T reduce(T state, Action action);
}
