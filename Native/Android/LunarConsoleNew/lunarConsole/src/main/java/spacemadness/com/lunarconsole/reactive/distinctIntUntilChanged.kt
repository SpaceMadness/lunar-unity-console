package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.core.IntMapper

/**
 * Suppress sequential duplicate items emitted by an Observable.
 *
 * This function does not accept lambdas to avoid unnecessary integer boxing in JVM. Each Kotlin
 * lambda is transformed to a generic FunctionX class: as a result each Int parameter is transformed
 * to Integer class.
 * @param function - mapper function which transforms generic parameter to a primitive integer.
 */
fun <T> Observable<T>.distinctIntUntilChanged(function: IntMapper<T>): Observable<T> {
    return ObservableDistinctIntUntilChanged(this, function)
}

private class ObservableDistinctIntUntilChanged<T>(
    private val source: Observable<T>,
    private val function: IntMapper<T>
) : Observable<T> {
    override fun subscribe(observer: Observer<T>): Disposable {
        // var prev: Int? = null - all nullable Int types are translated into Integer objects in JVM
        var prev = 0
        var firstTime = true
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