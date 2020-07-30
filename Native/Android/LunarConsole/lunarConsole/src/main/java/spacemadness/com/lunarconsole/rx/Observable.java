package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.IDisposable;

public interface Observable<T> {
    IDisposable subscribe(Observer<T> observer);
}
