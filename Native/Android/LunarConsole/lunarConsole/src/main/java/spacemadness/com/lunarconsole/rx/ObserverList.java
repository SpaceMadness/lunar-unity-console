//
//  ObserverList.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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


package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.utils.ConcurrentModificationList;

public final class ObserverList<T> extends ConcurrentModificationList<Observer<T>> {
    public ObserverList() {
        this(1);
    }

    public ObserverList(int capacity) {
        super(capacity);
    }

    public void notifyObservers(T value) {
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