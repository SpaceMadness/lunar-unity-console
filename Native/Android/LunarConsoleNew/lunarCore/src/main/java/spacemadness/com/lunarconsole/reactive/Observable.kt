package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

typealias Observer<T> = (T) -> Unit

interface Observable<out T> {
    fun subscribe(observer: Observer<T>): Disposable
}