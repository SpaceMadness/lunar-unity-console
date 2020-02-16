package spacemadness.com.lunarconsole.reactive

import org.junit.Test

import spacemadness.com.lunarconsole.test.TestCase

class ObservableMergeTest : TestCase() {
    @Test
    fun merge() {
        val a = PublishSubject<String>()
        val b = PublishSubject<String>()
        val c = PublishSubject<String>()

        val subscription = merge(a, b, c).subscribe { addResult(it) }

        a.post("a1")
        assertResults("a1")

        b.post("b1")
        assertResults("b1")

        b.post("b2")
        assertResults("b2")

        c.post("c1")
        assertResults("c1")

        a.post("a2")
        assertResults("a2")

        subscription.dispose()

        a.post("a3")
        b.post("b3")
        c.post("c3")

        assertResults()
    }
}