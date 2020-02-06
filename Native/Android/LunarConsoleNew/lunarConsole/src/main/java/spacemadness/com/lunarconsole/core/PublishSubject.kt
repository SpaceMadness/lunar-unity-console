package spacemadness.com.lunarconsole.core

class PublishSubject<T> : Subject<T>() {
    fun post(value: T) {
        notifyObservers(value)
    }
}