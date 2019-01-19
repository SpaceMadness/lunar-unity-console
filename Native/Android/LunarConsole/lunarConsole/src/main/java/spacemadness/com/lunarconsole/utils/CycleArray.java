//
//  CycleArray.java
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

package spacemadness.com.lunarconsole.utils;

import java.lang.reflect.Array;
import java.util.Iterator;

public class CycleArray<E> implements Iterable<E>
{
    private final Class<? extends E> componentType;
    private E[] internalArray;
    private int headIndex;
    private int length;

    public CycleArray(Class<? extends E> componentType, int capacity)
    {
        if (componentType == null)
        {
            throw new NullPointerException("Component type is null");
        }
        this.componentType = componentType;
        this.internalArray = (E[]) Array.newInstance(componentType, capacity);
    }

    public E add(E e)
    {
        int arrayIndex = toArrayIndex(length);
        E oldItem = internalArray[arrayIndex];
        internalArray[arrayIndex] = e;
        ++length;

        if (length - headIndex > internalArray.length) // array "overflows"
        {
            ++headIndex;
            return oldItem;
        }

        return null; // no items were destroyed
    }

    public void clear()
    {
        for (int i = 0; i < internalArray.length; ++i)
        {
            internalArray[i] = null;
        }
        length = 0;
        headIndex = 0;
    }

    public void trimLength(int trimSize)
    {
        trimToLength(length - trimSize);
    }

    public void trimToLength(int trimmedLength)
    {
        if (trimmedLength < headIndex || trimmedLength > length)
        {
            throw new IllegalArgumentException("Trimmed length " + trimmedLength +
                    " should be between head index " + headIndex +
                    " and length " + length);
        }

        length = trimmedLength;
    }

    public void trimHeadIndex(int trimSize)
    {
        trimToHeadIndex(headIndex + trimSize);
    }

    public void trimToHeadIndex(int trimmedHeadIndex)
    {
        if (trimmedHeadIndex < headIndex || trimmedHeadIndex > length)
        {
            throw new IllegalArgumentException("Trimmed head index " + trimmedHeadIndex +
                    " should be between head index " + headIndex +
                    " and length " + length);
        }

        headIndex = trimmedHeadIndex;
    }

    public E get(int index)
    {
        int arrayIndex = toArrayIndex(index);
        return internalArray[arrayIndex];
    }

    public void set(int index, E value)
    {
        int arrayIndex = toArrayIndex(index);
        internalArray[arrayIndex] = value;
    }

    public int toArrayIndex(int i)
    {
        return i % internalArray.length;
    }

    public boolean isValidIndex(int index)
    {
        return index >= headIndex && index < length;
    }

    private int toArrayIndex(E[] array, int i)
    {
        return i % array.length;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    public int getCapacity()
    {
        return internalArray.length;
    }

    public void setCapacity(int value)
    {
        if (value > getCapacity())
        {
            E[] data = (E[]) Array.newInstance(componentType, value);

            int totalCopyLength = realLength();

            int fromIndex = toArrayIndex(internalArray, headIndex);
            int toIndex = toArrayIndex(data, headIndex);

            while (totalCopyLength > 0)
            {
                int copyLength = Math.min(totalCopyLength, Math.min(internalArray.length - fromIndex, data.length - toIndex));

                System.arraycopy(internalArray, fromIndex, data, toIndex, copyLength);
                totalCopyLength -= copyLength;
                fromIndex = toArrayIndex(internalArray, fromIndex + copyLength);
                toIndex = toArrayIndex(data, toIndex + copyLength);
            }

            internalArray = data;
        }
        else if (value < getCapacity())
        {
            throw new NotImplementedException();
        }
    }

    public boolean contains(Object element)
    {
        for (int i = headIndex; i < length; ++i)
        {
            int arrayIndex = toArrayIndex(i);
            if (ObjectUtils.areEqual(internalArray[arrayIndex], element))
            {
                return true;
            }
        }

        return false;
    }

    public int getHeadIndex()
    {
        return headIndex;
    }

    public int length()
    {
        return length;
    }

    public int realLength()
    {
        return length - headIndex;
    }

    public E[] internalArray()
    {
        return internalArray;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Iterable

    @Override
    public Iterator<E> iterator()
    {
        return new CycleIterator();
    }

    private class CycleIterator implements Iterator<E>
    {
        private int index;

        public CycleIterator()
        {
            index = getHeadIndex();
        }

        @Override
        public boolean hasNext()
        {
            return index < length();
        }

        @Override
        public E next()
        {
            return get(index++);
        }

        @Override
        public void remove()
        {
            throw new NotImplementedException();
        }
    }
}