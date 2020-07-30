package spacemadness.com.lunarconsole.rx;

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;
import spacemadness.com.lunarconsole.core.Disposable;

public class SubjectTest extends TestCase {
    @Test
    public void TestObservers() {
        MockSubject<String> subject = new MockSubject<>();

        Disposable a = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A:" + value + "");
            }
        });
        Disposable b = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B:" + value + "");
            }
        });
        Disposable c = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C:" + value + "");
            }
        });

        subject.post("1");
        assertResults("A:1", "B:1", "C:1");

        b.Dispose();
        subject.post("2");
        assertResults("A:2", "C:2");

        a.Dispose();
        subject.post("3");
        assertResults("C:3");

        Disposable d = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("D:" + value + "");
            }
        });
        subject.post("4");
        assertResults("C:4", "D:4");

        c.Dispose();

        subject.post("5");
        assertResults("D:5");

        d.Dispose();
        subject.post("6");
        assertResults();
    }

    @Test
    public void TestObserversAllObservers() {
        MockSubject<String> subject = new MockSubject<>();
        Disposable a = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A:" + value + "");
            }
        });
        Disposable b = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B:" + value + "");
            }
        });
        Disposable c = subject.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C:" + value + "");
            }
        });

        subject.post("1");
        assertResults("A:1", "B:1", "C:1");

        subject.RemoveAllObservers();

        subject.post("2");
        assertResults();

        a.Dispose();
        b.Dispose();
        c.Dispose();

        subject.post("3");
        assertResults();
    }

    @Test
    public void TestRemoveObserverWhileNotifying() {
        MockSubject<String> observable = new MockSubject<String>();
        final Disposable subscriptionA = observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A:" + value + "");
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B:" + value + "");
                subscriptionA.Dispose();
            }
        });

        observable.post("1");
        assertResults("A:1", "B:1");

        observable.post("2");
        assertResults("B:2");
    }

    @Test
    public void TestRemoveAllObserversWhileNotifying() {
        final MockSubject<String> observable = new MockSubject<String>();
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("A:" + value + "");
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("B:" + value + "");
                observable.RemoveAllObservers();
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onChanged(String value) {
                addResult("C:" + value + "");
            }
        });

        observable.post("1");
        assertResults("A:1", "B:1");

        observable.post("2");
        assertResults();
    }

    private class MockSubject<T> extends Subject<T> {
        public void post(T value) {
            NotifyObservers(value);
        }
    }
}