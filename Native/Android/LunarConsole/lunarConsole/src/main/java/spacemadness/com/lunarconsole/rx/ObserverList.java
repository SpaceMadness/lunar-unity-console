package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.utils.ConcurrentModificationList;

public final class ObserverList<T> extends ConcurrentModificationList<Observer<T>> {
    public ObserverList() {
        this(1);
    }

    public ObserverList(int capacity) {
        super(capacity);
    }

    public void NotifyObservers(T value) {
        try {
            updating = true;
            int size = data.size();
            int i = 0;
            while (i < size) {
                Observer<T> e = data.get(i);
                if (e != null) {
                    e.onChanged(value);
                }

                ++i;
            }
        } finally {
            updating = false;

            // clean up 'tombstones'
            int i = data.size() - 1;
            while (removedCount > 0 && i >= 0) {
                if (data.get(i) == null) {
                    data.remove(i);
                    removedCount -= 1;
                }

                --i;
            }
        }
    }
}