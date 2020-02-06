package spacemadness.com.lunarconsole.core

interface Observable<T> {
    fun subscribe(observer: (T) -> Unit): Disposable
}