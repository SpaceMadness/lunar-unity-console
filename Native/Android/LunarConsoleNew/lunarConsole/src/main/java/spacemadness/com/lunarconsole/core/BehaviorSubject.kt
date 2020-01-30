package spacemadness.com.lunarconsole.core

class BehaviorSubject<T>(value: T) : Observable<T>(value) {
    override var value: T
        get() = super.value
        public set(value) {
            super.value = value
        }
}