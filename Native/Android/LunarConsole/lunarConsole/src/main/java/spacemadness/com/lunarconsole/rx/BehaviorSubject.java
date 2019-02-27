package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.NotImplementedException;

public class BehaviorSubject<T> extends Observable<T> implements Observer<T> {
	private T value;

	public BehaviorSubject(T value) {
		this.value = value;
	}

	@Override public void onNext(T value) {
		throw new NotImplementedException();
	}

	@Override public void onError(Exception error) {
		throw new NotImplementedException();
	}

	@Override public void onCompleted() {
		throw new NotImplementedException();
	}

	@Override public Disposable subscribe(Observer<T> observer) {
		throw new NotImplementedException();
	}

	public T getValue() {
		return value;
	}
}
