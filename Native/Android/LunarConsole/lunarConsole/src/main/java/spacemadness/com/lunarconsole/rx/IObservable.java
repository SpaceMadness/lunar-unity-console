package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.IDisposable;

// FIXME: java naming conventions
public interface IObservable<T> {
    IDisposable Subscribe(Observer<T> observer);
}
