package spacemadness.com.lunarconsole.reactive

import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore
import spacemadness.com.lunarconsole.test.TestCase

class SubjectTest : TestCase() {
    @Test
    fun testObservers() {
        val subject = MockSubject<Int>()
        val a = subject.subscribe { addResult("A:$it") }
        val b = subject.subscribe { addResult("B:$it") }
        val c = subject.subscribe { addResult("C:$it") }

        subject.post(1)
        assertResults("A:1", "B:1", "C:1")

        b.dispose()
        subject.post(2)
        assertResults("A:2", "C:2")

        a.dispose()
        subject.post(3)
        assertResults("C:3")

        val d = subject.subscribe { addResult("D:$it") }
        subject.post(4)
        assertResults("C:4", "D:4")

        c.dispose()

        subject.post(5)
        assertResults("D:5")

        d.dispose()
        subject.post(6)
        assertResults()
    }

    @Test
    fun testRemoveObserverWhileNotifying() {
        val observable = MockSubject<Int>()
        val subscriptionA = observable.subscribe {
            addResult("A$it")
        }
        observable.subscribe {
            addResult("B$it")
            subscriptionA.dispose()
        }

        observable.post(1)
        assertResults("A1", "B1")

        observable.post(2)
        assertResults("B2")
    }
}

private class MockSubject<T> : Subject<T>() {
    fun post(value: T) {
        super.notifyObservers(value)
    }
}