package spacemadness.com.lunarconsole.core

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class BehaviorSubjectTest : TestCase() {
    @Test
    fun testValue() {
        val observable = BehaviorSubject("1")
        // new observer should get current value
        val subscriptionA = observable.subscribe {
            addResult("A$it")
        }
        assertResults("A1")

        // new observer should get current value (but others - won't)
        val subscriptionB = observable.subscribe {
            addResult("B$it")
        }
        assertResults("B1")

        // new value
        observable.value = "2"
        assertResults("A2", "B2")

        // remove subscription
        subscriptionA.dispose()

        observable.value = "3"
        assertResults("B3")

        // remove subscription
        subscriptionB.dispose()

        observable.value = "4"
        assertResults()

        observable.subscribe {
            addResult("C$it")
        }
        assertResults("C4")
    }

    @Test
    fun testRemoveObserver() {
        val observer = { value: String ->
            addResult(value)
        }

        val observable = BehaviorSubject("1")
        observable.subscribe(observer)

        assertResults("1")

        observable.removeObserver(observer)

        observable.value = "2"
        assertResults()
    }

    @Test
    fun testRemoveObserverWhileNotifying() {
        val observable = BehaviorSubject("1")
        val subscriptionA = observable.subscribe {
            addResult("A$it")
        }
        assertResults("A1")

        observable.subscribe {
            addResult("B$it")
            subscriptionA.dispose()
        }
        assertResults("B1")

        observable.value = "2"
        assertResults("B2")
    }
}