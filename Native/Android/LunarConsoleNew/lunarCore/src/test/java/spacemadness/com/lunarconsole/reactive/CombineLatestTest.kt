package spacemadness.com.lunarconsole.reactive

import org.junit.Test
import spacemadness.com.lunarconsole.test.TestCase

class CombineLatestTest : TestCase() {
    @Test
    fun testCombineLatest() {
        val a = PublishSubject<String>()
        val b = PublishSubject<String>()
        val c = PublishSubject<String>()

        val subscription = combineLatest(a, b, c) { it.joinToString(" ") }
            .subscribe { addResult(it) }

        a.post("a1")
        assertResults()

        b.post("b1")
        assertResults()

        c.post("c1")
        assertResults("a1 b1 c1")

        c.post("c2")
        assertResults("a1 b1 c2")

        a.post("a2")
        assertResults("a2 b1 c2")

        b.post("b2")
        assertResults("a2 b2 c2")

        subscription.dispose()

        a.post("a3")
        b.post("b3")
        c.post("c3")

        assertResults()
    }
}