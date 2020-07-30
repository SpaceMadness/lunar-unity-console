package spacemadness.com.lunarconsole.rx;

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;
import spacemadness.com.lunarconsole.core.Disposable;

public class PublishSubjectTest extends TestCase {
    @Test
    public void TestValue() {
        PublishSubject<String> observable = new PublishSubject<>();
        observable.Post("1");

        // new observer should NOT get the value
        Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value + "");
            }
        });
        AssertResults();

        // the observer should get the value
        observable.Post("2");
        AssertResults("A2");

        // new observer should NOT get current value
        Disposable subscriptionB = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value + "");
            }
        });
        AssertResults();

        // new value
        observable.Post("3");
        AssertResults("A3", "B3");

        // remove subscription
        subscriptionA.dispose();

        observable.Post("4");
        AssertResults("B4");

        // remove subscription
        subscriptionB.dispose();

        observable.Post("5");
        AssertResults();

        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C" + value + "");
            }
        });
        AssertResults();

        observable.Post("6");
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

        observable.Post("1");
        AssertResults("1");

        observable.removeObserver(observer);

        observable.Post("2");
        AssertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        PublishSubject<String> observable = new PublishSubject<>();
        final Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value + "");
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value + "");
                subscriptionA.dispose();
            }
        });

        observable.Post("1");
        AssertResults("A1", "B1");

        observable.Post("2");
        AssertResults("B2");
    }

    private void AssertResults(String... args) {
        assertResults(args);
    }
}