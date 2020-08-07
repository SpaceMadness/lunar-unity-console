//
//  PublishSubjectTest.java
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

public class PublishSubjectTest extends TestCase {
    @Test
    public void TestValue() {
        PublishSubject<String> observable = new PublishSubject<>();
        observable.post("1");

        // new observer should NOT get the value
        Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value);
            }
        });
        AssertResults();

        // the observer should get the value
        observable.post("2");
        AssertResults("A2");

        // new observer should NOT get current value
        Disposable subscriptionB = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value);
            }
        });
        AssertResults();

        // new value
        observable.post("3");
        AssertResults("A3", "B3");

        // remove subscription
        subscriptionA.dispose();

        observable.post("4");
        AssertResults("B4");

        // remove subscription
        subscriptionB.dispose();

        observable.post("5");
        AssertResults();

        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C" + value);
            }
        });
        AssertResults();

        observable.post("6");
        AssertResults("C6");
    }

    @Test
    public void TestRemoveObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult(value);
            }
        };

        PublishSubject<String> observable = new PublishSubject<>();
        observable.subscribe(observer);

        observable.post("1");
        AssertResults("1");

        observable.removeObserver(observer);

        observable.post("2");
        AssertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        PublishSubject<String> observable = new PublishSubject<>();
        final Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value);
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value);
                subscriptionA.dispose();
            }
        });

        observable.post("1");
        AssertResults("A1", "B1");

        observable.post("2");
        AssertResults("B2");
    }

    private void AssertResults(String... args) {
        assertResults(args);
    }
}