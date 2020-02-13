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
        b.post("b1")
        c.post("c1")

        assertResults("a1 b1 c1")

        c.post("c2")
        b.post("b2")
        a.post("a2")

        assertResults("a2 b2 c2")

        a.post("a3")
        c.post("c3")
        b.post("b3")

        assertResults("a3 b3 c3")

        subscription.dispose()

        a.post("a4")
        b.post("b4")
        c.post("c4")

        assertResults()
    }
}