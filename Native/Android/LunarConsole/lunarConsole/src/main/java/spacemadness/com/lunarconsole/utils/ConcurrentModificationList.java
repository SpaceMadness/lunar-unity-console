package spacemadness.com.lunarconsole.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class ConcurrentModificationList<E> {
    protected final List<E> data;
    protected boolean updating;
    protected int removedCount;

    public ConcurrentModificationList(int capacity) {
        data = new ArrayList<>(capacity);
    }

    public void Add(E element) {
        data.add(element);
    }

    public boolean Remove(E element) {
        int index = data.indexOf(element);
        if (index != -1) {
            if (updating) {
                data.set(index, null); // replace element with 'tombstone'
                ++removedCount;
            } else {
                data.remove(index);
            }

            return true;
        }

        return false;
    }

    public void Clear() {
        if (updating) {
            int i = 0;
            while (i < data.size()) {
                data.set(i, null);
                ++removedCount;
                ++i;
            }
        } else {
            data.clear();
        }
    }

    public int Count() {
        return data.size();
    }

    public boolean IsEmpty() {
        return data.size() == 0;
    }
}