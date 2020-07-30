package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.IDisposable;

public abstract class Subject<T> implements IObservable<T> {
    private final ObserverList<T> observers = new ObserverList<T>();

    public IDisposable Subscribe(final Observer<T> observer) {
        observers.Add(observer);
        return new IDisposable() {
            @Override
            public void Dispose() {
                RemoveObserver(observer);
            }
        };
    }

    public void RemoveObserver(Observer<T> observer) {
        observers.Remove(observer);
    }

    public void RemoveAllObservers() {
        observers.Clear();
    }

    protected void NotifyObservers(T value) {
        observers.NotifyObservers(value);
    }

    protected void NotifyObserver(Observer<T> observer, T value) {
        observer.onChanged(value);
    }
}
