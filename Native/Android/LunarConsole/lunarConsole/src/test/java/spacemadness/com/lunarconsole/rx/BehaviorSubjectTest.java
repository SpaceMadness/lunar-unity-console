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
                addResult("A" + value + "");
            }
        });
        AssertResults("A1");

        // new observer should get current value (but others - won't)
        Disposable subscriptionB = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value + "");
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
                addResult("C" + value + "");
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

        observable.RemoveObserver(observer);

        observable.setValue("2");
        AssertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        BehaviorSubject<String> observable = new BehaviorSubject<>("1");

        final Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value + "");
            }
        });
        AssertResults("A1");

        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value + "");
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