//
//  ObserverListTest.java
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

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObserverListTest extends TestCase {
    @SafeVarargs
    private static ObserverList<String> concurrentListOf(Observer<String>... observables) {
        ObserverList<String> list = new ObserverList<>();
        for (Observer<String> observable : observables) {
            list.add(observable);
        }

        return list;
    }

    @Test
    public void TestNotifyObservers() {
        Observer<String> a = Callback("a");
        Observer<String> b = Callback("b");
        Observer<String> c = Callback("c");

        ObserverList<String> list = concurrentListOf(a, b, c);

        assertEquals(3, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("1");
        assertResults("a:1", "b:1", "c:1");

        list.notifyObservers("2");
        assertResults("a:2", "b:2", "c:2");
    }

    @Test
    public void TestAdd() {
        ObserverList<String> list = concurrentListOf();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.notifyObservers("1");
        assertResults();

        list.add(Callback("a"));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("2");
        assertResults("a:2");

        list.add(Callback("b"));
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("3");
        assertResults("a:3", "b:3");

        list.add(Callback("c"));
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("4");
        assertResults("a:4", "b:4", "c:4");
    }

    @Test
    public void TestRemove() {
        Observer<String> a = Callback("a");
        Observer<String> b = Callback("b");
        Observer<String> c = Callback("c");

        ObserverList<String> list = concurrentListOf(a, b, c);

        list.remove(b);
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("1");
        assertResults("a:1", "c:1");

        list.remove(a);
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("2");
        assertResults("c:2");

        list.remove(a);
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("3");
        assertResults("c:3");

        list.remove(c);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.notifyObservers("3");
        assertResults();
    }

    @Test
    public void TestRemoveWhileNotifying() {
        final ObserverList<String> list = concurrentListOf();

        final Observer<String> a = Callback("a");
        final Observer<String> c = Callback("c");
        Observer<String> b = Callback("b", new Observer<String>() {
            @Override
            public void onChanged(String value) {
                list.remove(a);
                list.remove(c);
            }
        });

        list.add(a);
        list.add(b);
        list.add(c);

        list.notifyObservers("1");
        assertResults("a:1", "b:1");

        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.notifyObservers("2");
        assertResults("b:2");
    }

    @Test
    public void TestClearWhileNotifying() {
        final ObserverList<String> list = concurrentListOf();

        Observer<String> a = Callback("a");
        Observer<String> c = Callback("c");
        Observer<String> b = Callback("b", new Observer<String>() {
            @Override
            public void onChanged(String value) {
                list.clear();
            }
        });

        list.add(a);
        list.add(b);
        list.add(c);

        list.notifyObservers("1");
        assertResults("a:1", "b:1");

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.notifyObservers("2");
        assertResults();
    }

    private Observer<String> Callback(String name) {
        return Callback(name, null);
    }

    private Observer<String> Callback(final String name, final Observer<String> callback) {
        return new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult(name + ":" + value);
                if (callback != null) {
                    callback.onChanged(value);
                }
            }
        };
    }
}