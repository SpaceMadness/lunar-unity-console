package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction;

final class MapObservable<T, R> implements Observable<R> {
    private final Observable<T> source;
    private final MapFunction<T, R> function;

    public MapObservable(Observable<T> source, MapFunction<T, R> function) {
        if (source == null) {
            throw new IllegalArgumentException("Source is null");
        }
        if (function == null) {
            throw new IllegalArgumentException("Mapping function is null");
        }
        this.source = source;
        this.function = function;
    }

    public Disposable subscribe(final Observer<R> observer) {
        return source.subscribe(new Observer<T>() {
            @Override
            public void onChanged(T value) {
                observer.onChanged(function.map(value));
            }
        });
    }
}
