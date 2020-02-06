package spacemadness.com.lunarconsole.core

import spacemadness.com.lunarconsole.utils.concurrentListOf

abstract class Subject<T> : Observable<T> {
    private val observers = concurrentListOf<(T) -> Unit>()

    override fun subscribe(observer: (T) -> Unit): Disposable {
        observers.add(observer)

        return object : Disposable {
            override fun dispose() {
                removeObserver(observer)
            }
        }
    }

    fun removeObserver(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    protected fun notifyObservers(value: T) {
        observers.forEach { _, observer ->
            notifyObserver(observer, value)
        }
    }

    protected fun notifyObserver(observer: (T) -> Unit, value: T) {
        observer(value)
    }
}