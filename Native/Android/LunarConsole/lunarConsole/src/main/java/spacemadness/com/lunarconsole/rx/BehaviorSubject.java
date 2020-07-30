package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.IDisposable;

public final class BehaviorSubject<T> extends Subject<T> {
    private T m_value;

    public BehaviorSubject() {
        this(null);
    }

    public BehaviorSubject(T value) {
        m_value = value;
    }

    @Override
    public IDisposable Subscribe(Observer<T> observer) {
        IDisposable disposable = super.Subscribe(observer);
        NotifyObserver(observer, getValue());
        return disposable;
    }

    public T getValue() {
        return m_value;
    }

    public void setValue(T value) {
        m_value = value;
        NotifyObservers(value);
    }
}