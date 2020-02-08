package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.core.IntMapper

fun <T> Observable<T>.distinctIntUntilChanged(function: IntMapper<T>): Observable<T> {
    return ObservableDistinctIntUntilChanged(this, function)
}

private class ObservableDistinctIntUntilChanged<T>(
    private val source: Observable<T>,
    private val function: IntMapper<T>
) : Observable<T> {
    override fun subscribe(observer: Observer<T>): Disposable {
        // var prev: Int? = null - all nullable types are translated into objects for JVM
        var prev = 0
        var firstTime = false
        val subscription = source.subscribe {
            val curr = function.map(it)
            if (prev != curr || firstTime) {
                prev = curr
                firstTime = false
                observer.invoke(it)
            }
        }
        return WrapperDisposable(subscription)
    }
}