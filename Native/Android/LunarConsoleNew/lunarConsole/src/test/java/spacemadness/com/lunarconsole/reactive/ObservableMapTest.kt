package spacemadness.com.lunarconsole.reactive

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class ObservableMapTest : TestCase() {
    @Test
    fun testMap() {
        val stream = PublishSubject<Int>()
        val subscription = stream
            .map { it.toString() }
            .subscribe { addResult(it) }

        stream.post(1)
        stream.post(2)
        stream.post(3)
        assertResults("1", "2", "3")

        subscription.dispose()

        stream.post(4)
        stream.post(5)
        stream.post(6)
        assertResults()
    }
}