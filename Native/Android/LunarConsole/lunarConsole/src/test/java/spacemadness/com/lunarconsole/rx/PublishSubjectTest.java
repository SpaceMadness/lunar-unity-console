package spacemadness.com.lunarconsole.rx;

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;
import spacemadness.com.lunarconsole.core.IDisposable;

public class PublishSubjectTest extends TestCase {
    @Test
    public void TestValue() {
        PublishSubject<String> observable = new PublishSubject<>();
        observable.Post("1");

        // new observer should NOT get the value
        IDisposable subscriptionA = observable.Subscribe(new Observer<String>() {
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
        IDisposable subscriptionB = observable.Subscribe(new Observer<String>() {
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
        subscriptionA.Dispose();

        observable.Post("4");
        AssertResults("B4");

        // remove subscription
        subscriptionB.Dispose();

        observable.Post("5");
        AssertResults();

        observable.Subscribe(new Observer<String>() {
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
        observable.Subscribe(observer);

        observable.Post("1");
        AssertResults("1");

        observable.RemoveObserver(observer);

        observable.Post("2");
        AssertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        PublishSubject<String> observable = new PublishSubject<>();
        final IDisposable subscriptionA = observable.Subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A" + value + "");
            }
        });
        observable.Subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B" + value + "");
                subscriptionA.Dispose();
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