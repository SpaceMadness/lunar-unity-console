package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

fun <T> Observable<T>.filter(predicate: (T) -> Boolean): Observable<T> {
    return FilterObservable(this, predicate)
}

private class FilterObservable<T>(
    private val source: Observable<T>,
    private val predicate: (T) -> Boolean
) : Observable<T> {
    override fun subscribe(observer: Observer<T>): Disposable {
        val subscription = source.subscribe {
            if (predicate(it)) {
                observer.invoke(it)
            }
        }
        return WrapperDisposable(subscription)
    }
}