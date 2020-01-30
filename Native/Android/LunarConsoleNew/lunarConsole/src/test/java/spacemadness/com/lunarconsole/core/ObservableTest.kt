package spacemadness.com.lunarconsole.core

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class ObservableTest : TestCase() {
    @Test
    fun testValue() {
        val observable = BehaviorSubject("1")
        // new observer should get current value
        val subscriptionA = observable.observe {
            addResult("A$it")
        }
        assertResults("A1")

        // new observer should get current value (but others - won't)
        val subscriptionB = observable.observe {
            addResult("B$it")
        }
        assertResults("B1")

        // new value
        observable.value = "2"
        assertResults("A2", "B2")

        // remove subscription
        subscriptionA.unsubscribe()

        observable.value = "3"
        assertResults("B3")

        // remove subscription
        subscriptionB.unsubscribe()

        observable.value = "4"
        assertResults()

        observable.observe {
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
        observable.observe(observer)

        assertResults("1")

        observable.removeObserver(observer)

        observable.value = "2"
        assertResults()
    }
}