package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

fun <T, R> Observable<T>.map(function: (T) -> R): Observable<R> {
    return ObservableMap(this, function)
}

private class ObservableMap<T, R>(
    private val source: Observable<T>,
    private val function: (T) -> R
) : Observable<R> {
    override fun subscribe(observer: Observer<R>): Disposable {
        val subscription = source.subscribe {
            observer.invoke(function(it))
        }
        return WrapperDisposable(subscription)
    }
}