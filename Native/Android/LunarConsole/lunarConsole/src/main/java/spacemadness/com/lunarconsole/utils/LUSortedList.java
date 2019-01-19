//
//  LUSortedList.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LUSortedList<T extends Comparable> implements Iterable<T>
{
    private final List<T> list;
    private boolean sortingEnabled = true;

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
        if (sortingEnabled)
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

    public List<T> list()
    {
        return list;
    }

    public boolean isSortingEnabled()
    {
        return sortingEnabled;
    }

    public void setSortingEnabled(boolean sortingEnabled)
    {
        this.sortingEnabled = sortingEnabled;
    }

    //region Iterable

    @Override
    public Iterator<T> iterator()
    {
        return list.iterator();
    }

    //endregion
}
