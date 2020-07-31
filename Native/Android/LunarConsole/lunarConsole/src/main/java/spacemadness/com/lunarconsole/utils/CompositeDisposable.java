package spacemadness.com.lunarconsole.utils;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.core.Disposable;

public class CompositeDisposable implements Disposable {
    private final List<Disposable> disposables;

    public CompositeDisposable() {
        disposables = new ArrayList<>();
    }

    public void add(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
    }
}
