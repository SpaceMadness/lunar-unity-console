package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

public abstract class Subject<T> implements Observable<T> {
    private final ObserverList<T> observers = new ObserverList<T>();

    public Disposable subscribe(final Observer<T> observer) {
        observers.Add(observer);
        return new Disposable() {
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
