package spacemadness.com.lunarconsole.core

import org.junit.Test
import spacemadness.com.lunarconsole.test.TestCase
import spacemadness.com.lunarconsole.reactive.PublishSubject

class PublishSubjectTest : TestCase() {
    @Test
    fun testValue() {
        val observable = PublishSubject<String>()
        observable.post("1")

        // new observer should NOT get the value
        val subscriptionA = observable.subscribe {
            addResult("A$it")
        }
        assertResults()

        // the observer should get the value
        observable.post("2")
        assertResults("A2")

        // new observer should NOT get current value
        val subscriptionB = observable.subscribe {
            addResult("B$it")
        }
        assertResults()

        // new value
        observable.post("3")
        assertResults("A3", "B3")

        // remove subscription
        subscriptionA.dispose()

        observable.post("4")
        assertResults("B4")

        // remove subscription
        subscriptionB.dispose()

        observable.post("5")
        assertResults()

        observable.subscribe {
            addResult("C$it")
        }
        assertResults()

        observable.post("6")
        assertResults("C6")
    }

    @Test
    fun testRemoveObserver() {
        val observer = { value: String ->
            addResult(value)
        }

        val observable = PublishSubject<String>()
        observable.subscribe(observer)

        observable.post("1")
        assertResults("1")

        observable.removeObserver(observer)

        observable.post("2")
        assertResults()
    }

    @Test
    fun testRemoveObserverWhileNotifying() {
        val observable = PublishSubject<String>()
        val subscriptionA = observable.subscribe {
            addResult("A$it")
        }
        observable.subscribe {
            addResult("B$it")
            subscriptionA.dispose()
        }

        observable.post("1")
        assertResults("A1", "B1")

        observable.post("2")
        assertResults("B2")
    }
}