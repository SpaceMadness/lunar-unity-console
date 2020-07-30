package spacemadness.com.lunarconsole.rx;

import org.junit.Test;

import spacemadness.com.lunarconsole.TestCase;

import static org.junit.Assert.*;

public class ObserverListTest extends TestCase {
    @Test
    public void TestNotifyObservers() {
        Observer<String> a = Callback("a");
        Observer<String> b = Callback("b");
        Observer<String> c = Callback("c");

        ObserverList<String> list = concurrentListOf(a, b, c);

        assertEquals(3, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("1");
        assertResults("a:1", "b:1", "c:1");

        list.NotifyObservers("2");
        assertResults("a:2", "b:2", "c:2");
    }

    @Test
    public void TestAdd() {
        ObserverList<String> list = concurrentListOf();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.NotifyObservers("1");
        assertResults();

        list.Add(Callback("a"));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("2");
        assertResults("a:2");

        list.Add(Callback("b"));
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("3");
        assertResults("a:3", "b:3");

        list.Add(Callback("c"));
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("4");
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

        list.NotifyObservers("1");
        assertResults("a:1", "c:1");

        list.remove(a);
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("2");
        assertResults("c:2");

        list.remove(a);
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("3");
        assertResults("c:3");

        list.remove(c);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.NotifyObservers("3");
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

        list.Add(a);
        list.Add(b);
        list.Add(c);

        list.NotifyObservers("1");
        assertResults("a:1", "b:1");

        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        list.NotifyObservers("2");
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

        list.Add(a);
        list.Add(b);
        list.Add(c);

        list.NotifyObservers("1");
        assertResults("a:1", "b:1");

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.NotifyObservers("2");
        assertResults();
    }

    @SafeVarargs
    private static ObserverList<String> concurrentListOf(Observer<String>... observables) {
        ObserverList<String> list = new ObserverList<>();
        for (Observer<String> observable : observables) {
            list.Add(observable);
        }

        return list;
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