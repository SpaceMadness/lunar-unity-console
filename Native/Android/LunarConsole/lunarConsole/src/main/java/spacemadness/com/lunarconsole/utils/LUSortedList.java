package spacemadness.com.lunarconsole.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by weeebox on 12/13/16.
 */

public class LUSortedList<T extends Comparable<T>>
{
    private final List<T> list;

    public LUSortedList()
    {
        list = new ArrayList<>();
    }

    public T objectAtIndex(int index)
    {
        return list.get(index);
    }

    public int addObject(T object)
    {
        // TODO: use binary search to insert in a sorted order
        for (int i = 0; i < list.size(); ++i)
        {
            int comparisonResult = object.compareTo(list.get(i));
            if (comparisonResult < 0)
            {
                list.add(i, object);
                return i;
            }
            else if (comparisonResult == 0)
            {
                list.set(i, object);
                return i;
            }
        }

        list.add(object);
        return list.size() - 1;
    }

    public void removeObject(T object)
    {
        list.remove(object);
    }

    public void removeObjectAtIndex(int index)
    {
        list.remove(index);
    }

    public void removeAllObjects()
    {
        list.clear();
    }

    public int indexOfObject(T object)
    {
        return list.indexOf(object);
    }

    public int count()
    {
        return list.size();
    }
}
