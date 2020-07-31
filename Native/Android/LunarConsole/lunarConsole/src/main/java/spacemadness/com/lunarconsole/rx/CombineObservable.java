package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.CompositeDisposable;

public class CombineObservable<T> implements Observable<T> {
    private final Observable<T>[] observables;
    private final Object[] values;

    public CombineObservable(Observable<T>[] observables) {
        this.observables = observables;
        values = new Object[observables.length];
    }

    @Override
    public Disposable subscribe(Observer<T> observer) {
        CompositeDisposable disposable = new CompositeDisposable();
        for (int i = 0; i < observables.length; i++) {
            final int observableIndex = i;
            Disposable subscription = observables[observableIndex].subscribe(new Observer<T>() {
                @Override
                public void onChanged(T value) {
                    values[observableIndex] = value;
                }
            });
            disposable.add(subscription);
        }
        return disposable;
    }
}
