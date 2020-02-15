package spacemadness.com.lunarconsole.reactive

import org.junit.Test

import org.junit.Assert.*
import spacemadness.com.lunarconsole.test.TestCase

class FilterObservableTest : TestCase() {
    @Test
    fun filter() {
        val stream = PublishSubject<Int>()
        val subscription = stream
            .filter { it % 2 == 0 }
            .subscribe { addResult(it) }

        stream.post(1)
        stream.post(2)
        stream.post(3)
        stream.post(4)
        stream.post(5)
        stream.post(6)
        assertResults(2, 4, 6)

        subscription.dispose()

        stream.post(7)
        stream.post(8)
        stream.post(9)
        assertResults()
    }
}