package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

public abstract class Subject<T> implements Observable<T> {
    private final ObserverList<T> observers = new ObserverList<T>();

    public Disposable subscribe(final Observer<T> observer) {
        observers.add(observer);
        return new Disposable() {
            @Override
            public void dispose() {
                removeObserver(observer);
            }
        };
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void notifyObservers(T value) {
        observers.notifyObservers(value);
    }

    protected void notifyObserver(Observer<T> observer, T value) {
        observer.onChanged(value);
    }
}
