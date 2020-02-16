package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

fun <T> merge(vararg observables: Observable<T>): Observable<T> = ObservableMerge(observables)

private class ObservableMerge<T>(
    private val observables: Array<out Observable<T>>
) : Observable<T> {
    override fun subscribe(observer: Observer<T>): Disposable {
        val subscriptions = observables.map { observable ->
            observable.subscribe(observer)
        }

        return CompositeDisposable(subscriptions)
    }
}