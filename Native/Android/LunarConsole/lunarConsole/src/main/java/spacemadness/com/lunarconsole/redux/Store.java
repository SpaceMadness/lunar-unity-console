package spacemadness.com.lunarconsole.redux;

import spacemadness.com.lunarconsole.rx.BehaviorSubject;
import spacemadness.com.lunarconsole.rx.Observable;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class Store<T extends State> {
	private final Reducer<T> reducer;
	private T state;
	private final BehaviorSubject<T> stateSubject;

	public Store(Reducer<T> reducer, T initialState) {
		this.reducer = checkNotNull(reducer, "reducer");
		this.state = checkNotNull(initialState, "initialState");
		stateSubject = new BehaviorSubject<>(state);
	}

	public void dispatchAction(final Action action) {
		try {
			state = reducer.reduce(state, action);
			stateSubject.onNext(state);
		} catch (Exception e) {
			stateSubject.onError(e);
		}
	}

	public Observable<T> getState() {
		return stateSubject;
	}
}
