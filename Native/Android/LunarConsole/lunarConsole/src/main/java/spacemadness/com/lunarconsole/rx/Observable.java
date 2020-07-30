package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

public interface Observable<T> {
    Disposable subscribe(Observer<T> observer);
}
