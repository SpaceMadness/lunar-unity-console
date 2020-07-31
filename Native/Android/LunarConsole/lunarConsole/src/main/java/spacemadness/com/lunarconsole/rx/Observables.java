package spacemadness.com.lunarconsole.rx;

import java.util.List;

import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction;

public class Observables {
    public static <T, R> Observable<R> map(Observable<T> observable, MapFunction<T, R> function) {
        return new MapObservable<>(observable, function);
    }

    @SafeVarargs
    public static <T> Observable<List<T>> combineLatest(Observable<T>... observables) {
        return new CombineLatestObservable<>(observables);
    }
}
