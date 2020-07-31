package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction;

public class Observables {
    public static <T, R> Observable<R> map(Observable<T> observable, MapFunction<T, R> function) {
        return new MapObservable<>(observable, function);
    }

    public static <T> Observable<T> combine(Observable<T>... observables) {
        return new CombineObservable<>(observables);
    }
}
