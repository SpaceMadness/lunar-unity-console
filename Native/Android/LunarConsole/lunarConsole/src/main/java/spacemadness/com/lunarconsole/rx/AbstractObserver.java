package spacemadness.com.lunarconsole.rx;

public class AbstractObserver<T> implements Observer<T> {
	@Override
	public void onNext(T value) {
	}

	@Override
	public void onError(Exception error) {
	}

	@Override
	public void onCompleted() {
	}
}
