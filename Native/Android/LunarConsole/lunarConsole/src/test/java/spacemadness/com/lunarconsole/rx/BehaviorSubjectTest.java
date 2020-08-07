//
//  BehaviorSubjectTest.java
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
import spacemadness.com.lunarconsole.core.Disposable;

public class BehaviorSubjectTest extends TestCase {
    @Test
    public void TestValue() {
        BehaviorSubject<String> observable = new BehaviorSubject<>("1");
        // new observer should get current value
        Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value);
            }
        });
        AssertResults("A1");

        // new observer should get current value (but others - won't)
        Disposable subscriptionB = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value);
            }
        });
        AssertResults("B1");

        // new value
        observable.setValue("2");
        AssertResults("A2", "B2");

        // remove subscription
        subscriptionA.dispose();

        observable.setValue("3");
        AssertResults("B3");

        // remove subscription
        subscriptionB.dispose();

        observable.setValue("4");
        AssertResults();

        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C" + value);
            }
        });
        AssertResults("C4");
    }

    @Test
    public void TestRemoveObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult(value);
            }
        };

        BehaviorSubject<String> observable = new BehaviorSubject<>("1");
        observable.subscribe(observer);

        AssertResults("1");

        observable.removeObserver(observer);

        observable.setValue("2");
        AssertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        BehaviorSubject<String> observable = new BehaviorSubject<>("1");

        final Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value);
            }
        });
        AssertResults("A1");

        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value);
                subscriptionA.dispose();
            }
        });
        AssertResults("B1");

        observable.setValue("2");
        AssertResults("B2");
    }

    private void AssertResults(String... rags) {
        assertResults(rags);
    }
}