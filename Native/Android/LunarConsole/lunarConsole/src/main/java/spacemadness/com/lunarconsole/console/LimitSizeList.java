package spacemadness.com.lunarconsole.console;

import java.util.Iterator;

import spacemadness.com.lunarconsole.utils.CycleArray;

public class LimitSizeList<T> implements Iterable<T>
{
    private final CycleArray<T> internalArray;

    public LimitSizeList(Class<? extends T> cls, int capacity)
    {
        if (capacity < 0)
        {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }

        internalArray = new CycleArray<>(cls, capacity);
    }

    public T objectAtIndex(int index)
    {
        return internalArray.get(internalArray.HeadIndex() + index);
    }

    public void addObject(T object)
    {
        internalArray.Add(object);
    }

    public void trimHead(int count)
    {
        internalArray.TrimHeadIndex(count);
    }

    public void clear()
    {
        internalArray.Clear();
    }

    @Override
    public Iterator<T> iterator()
    {
        return internalArray.iterator();
    }

    public int capacity()
    {
        return internalArray.Capacity();
    }

    public int totalCount()
    {
        return internalArray.Length();
    }

    public int count()
    {
        return internalArray.RealLength();
    }

    public int overflowCount()
    {
        return internalArray.HeadIndex();
    }

    public boolean isOverfloating()
    {
        return internalArray.HeadIndex() > 0 && willOverflow();
    }

    public boolean willOverflow()
    {
        return internalArray.RealLength() == internalArray.Capacity();
    }
}
