package spacemadness.com.lunarconsole.core

import androidx.annotation.WorkerThread

open class Observable<T>(private var _value: T) {
    private val observers = mutableListOf<(T) -> Unit>()

    open var value: T
        @WorkerThread get() = _value
        @WorkerThread protected set(value) {
            _value = value
            notifyObservers(value)
        }


    @WorkerThread
    fun observe(observer: (T) -> Unit): Subscription {
        observers.add(observer)
        notifyObserver(observer, value)

        return object : Subscription {
            override fun unsubscribe() {
                removeObserver(observer)
            }
        }
    }

    @WorkerThread
    fun removeObserver(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    @WorkerThread
    private fun notifyObservers(value: T) {
        val temp = observers.toList()
        for (observer in temp) {
            notifyObserver(observer, value)
        }
    }

    private fun notifyObserver(observer: (T) -> Unit, value: T) {
        observer(value)
    }
}

interface Subscription {
    fun unsubscribe()
}