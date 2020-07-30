package spacemadness.com.lunarconsole.rx;

public final class PublishSubject<T> extends Subject<T> {
    public void Post(T value) {
        NotifyObservers(value);
    }
}