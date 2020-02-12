package spacemadness.com.lunarconsole.reactive

class PublishSubject<T> : Subject<T>() {
    fun post(value: T) {
        notifyObservers(value)
    }
}