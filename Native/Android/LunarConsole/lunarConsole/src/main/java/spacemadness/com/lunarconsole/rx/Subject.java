//
//  Subject.java
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


package spacemadness.com.lunarconsole.rx;

import spacemadness.com.lunarconsole.core.Disposable;

public abstract class Subject<T> implements Observable<T> {
    private final ObserverList<T> observers = new ObserverList<T>();

    public Disposable subscribe(final Observer<T> observer) {
        observers.add(observer);
        return new Disposable() {
            @Override
            public void dispose() {
                removeObserver(observer);
            }
        };
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void notifyObservers(T value) {
        observers.notifyObservers(value);
    }

    protected void notifyObserver(Observer<T> observer, T value) {
        observer.onChanged(value);
    }
}
