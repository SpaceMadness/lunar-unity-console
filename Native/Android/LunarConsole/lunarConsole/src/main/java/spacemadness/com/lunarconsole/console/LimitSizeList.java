//
//  LimitSizeList.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.console;

import java.util.Iterator;

import spacemadness.com.lunarconsole.utils.CycleArray;

public class LimitSizeList<T> implements Iterable<T>
{
    private final CycleArray<T> internalArray;
    private final int trimSize;

    public LimitSizeList(Class<? extends T> cls, int capacity, int trimSize)
    {
        if (capacity < 0)
        {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }

        this.internalArray = new CycleArray<>(cls, capacity);
        this.trimSize = trimSize;
    }

    public T objectAtIndex(int index)
    {
        return internalArray.get(internalArray.getHeadIndex() + index);
    }

    public void addObject(T object)
    {
        if (willOverflow())
        {
            trimHead(trimSize);
        }
        internalArray.add(object);
    }

    public void trimHead(int count)
    {
        internalArray.trimHeadIndex(count);
    }

    public void clear()
    {
        internalArray.clear();
    }

    @Override
    public Iterator<T> iterator()
    {
        return internalArray.iterator();
    }

    public int capacity()
    {
        return internalArray.getCapacity();
    }

    public int totalCount()
    {
        return internalArray.length();
    }

    public int count()
    {
        return internalArray.realLength();
    }

    public int getTrimSize()
    {
        return trimSize;
    }

    public int overflowCount()
    {
        return internalArray.getHeadIndex();
    }

    public boolean isOverfloating()
    {
        return internalArray.getHeadIndex() > 0 && willOverflow();
    }

    public boolean willOverflow()
    {
        return internalArray.realLength() == internalArray.getCapacity();
    }

    public boolean isTrimmed()
    {
        return trimmedCount() > 0;
    }

    public int trimmedCount()
    {
        return totalCount() - count();
    }
}
