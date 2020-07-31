package spacemadness.com.lunarconsole.rx;

import java.util.List;

import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.CollectionUtils;
import spacemadness.com.lunarconsole.utils.CompositeDisposable;

public class CombineLatestObservable<T> extends Observable<List<T>> {
    private final Observable<T>[] observables;
    private final List<T> latestValues;
    private final boolean[] hasValues;

    public CombineLatestObservable(Observable<T>[] observables) {
        this.observables = observables;
        latestValues = CollectionUtils.listOf(null, observables.length);
        hasValues = new boolean[observables.length];
    }

    @Override
    public Disposable subscribe(final Observer<List<T>> observer) {
        CompositeDisposable disposable = new CompositeDisposable();
        for (int i = 0; i < observables.length; i++) {
            final int observableIndex = i;
            Disposable subscription = observables[observableIndex].subscribe(new Observer<T>() {
                @Override
                public void onChanged(T value) {
                    hasValues[observableIndex] = true;
                    latestValues.set(observableIndex, value);

                    if (hasAllValues()) {
                        observer.onChanged(latestValues);
                    }
                }
            });
            disposable.add(subscription);
        }
        return disposable;
    }

    private boolean hasAllValues() {
        for (int i = 0; i < hasValues.length; ++i) {
            if (!hasValues[i]) {
                return false;
            }
        }
        return true;
    }
}
