package spacemadness.com.lunarconsole.core

typealias Observer<T> = (T) -> Unit

interface Observable<T> {
    fun subscribe(observer: Observer<T>): Disposable
}