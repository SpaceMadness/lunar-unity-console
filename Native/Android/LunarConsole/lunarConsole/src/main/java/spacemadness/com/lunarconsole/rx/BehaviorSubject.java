package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

public final class BehaviorSubject<T> extends Subject<T> {
    private T value;

    public BehaviorSubject() {
        this(null);
    }

    public BehaviorSubject(T value) {
        this.value = value;
    }

    @Override
    public Disposable subscribe(Observer<T> observer) {
        Disposable disposable = super.subscribe(observer);
        notifyObserver(observer, getValue());
        return disposable;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        notifyObservers(value);
    }
}