package spacemadness.com.lunarconsole.rx;

import java.util.List;

import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.CollectionUtils;

public abstract class Observable<T> {
    public abstract Disposable subscribe(Observer<T> observer);

    public <R> Observable<R> map(CollectionUtils.MapFunction<T, R> function) {
        return new MapObservable<>(this, function);
    }

    @SafeVarargs
    public static <T> Observable<List<T>> combineLatest(Observable<T>... observables) {
        return new CombineLatestObservable<>(observables);
    }
}
