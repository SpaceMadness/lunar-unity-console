package spacemadness.com.lunarconsole.core

import org.junit.Assert.assertTrue
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.concurrent.BlockingDispatchQueue
import spacemadness.com.lunarconsole.concurrent.DispatchQueue

class StreamTest : TestCase() {

    @Test
    fun testSubscription() {
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        val stream = MutableStream("a")

        val subscription1 = listen(stream, "1", dispatchQueue)
        assertResults("1: onValue(a)")

        val subscription2 = listen(stream, "2", dispatchQueue)
        assertResults("2: onValue(a)")

        val subscription3 = listen(stream, "3", dispatchQueue)
        assertResults("3: onValue(a)")

        stream.addValue("b")
        assertResults("1: onValue(b)", "2: onValue(b)", "3: onValue(b)")

        subscription2.dispose()

        stream.addValue("c")
        assertResults("1: onValue(c)", "3: onValue(c)")

        subscription1.dispose()

        stream.addValue("d")
        assertResults("3: onValue(d)")

        stream.close()
        assertResults("3: onDone()")

        stream.addValue("e")
        assertResults()
    }

    private fun <T> listen(stream: Stream<T>, name: String, dispatchQueue: DispatchQueue? = null): StreamSubscription<T> {
        return stream.listen(dispatchQueue,
                StreamSubscription.OnValue<T> { value ->
                    if (dispatchQueue != null) {
                        assertTrue(dispatchQueue.isCurrent)
                    }
                    addResult("$name: onValue($value)")
                },
                StreamSubscription.OnError { error ->
                    if (dispatchQueue != null) {
                        assertTrue(dispatchQueue.isCurrent)
                    }
                    addResult("$name: onError($error)")
                },
                StreamSubscription.OnDone {
                    if (dispatchQueue != null) {
                        assertTrue(dispatchQueue.isCurrent)
                    }
                    addResult("$name: onDone()")
                })
    }
}