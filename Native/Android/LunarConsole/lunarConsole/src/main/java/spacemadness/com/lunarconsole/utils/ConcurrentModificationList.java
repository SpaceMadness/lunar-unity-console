//
//  ConcurrentModificationList.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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
import java.util.List;

public abstract class ConcurrentModificationList<E> {
    protected final List<E> data;
    protected boolean updating;
    protected int removedCount;

    public ConcurrentModificationList(int capacity) {
        data = new ArrayList<>(capacity);
    }

    public void add(E element) {
        data.add(element);
    }

    public boolean remove(E element) {
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

    public void clear() {
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

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.size() == 0;
    }
}