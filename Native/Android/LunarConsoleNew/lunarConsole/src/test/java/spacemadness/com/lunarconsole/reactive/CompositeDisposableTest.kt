package spacemadness.com.lunarconsole.reactive

import org.junit.Test

import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.core.Disposable

class CompositeDisposableTest : TestCase() {
    @Test
    fun testAdd() {
        val disposable = CompositeDisposable()

        val a = createDisposable("a")
        disposable.add(a)

        val b = createDisposable("b")
        disposable.add(b)

        val c = createDisposable("c")
        disposable.add(c)

        disposable.dispose()
        assertResults("a", "b", "c")

        val d = createDisposable("d")
        disposable.add(d)

        val e = createDisposable("e")
        disposable.add(e)

        disposable.dispose()

        assertResults("d", "e")
    }

    @Test
    fun testAddBatch() {
        val a = createDisposable("a")
        val b = createDisposable("b")
        val c = createDisposable("c")

        val disposable = CompositeDisposable()
        disposable.add(a, b, c)
        disposable.dispose()
        assertResults("a", "b", "c")

        val d = createDisposable("d")
        val e = createDisposable("e")

        disposable.add(d, e)
        disposable.dispose()

        assertResults("d", "e")
    }

    @Test
    fun testAddDuplicates() {
        val a = createDisposable("a")
        val b = createDisposable("b")
        val c = createDisposable("c")

        val disposable = CompositeDisposable()
        disposable.add(a, a, b, b, c)
        disposable.dispose()
        assertResults("a", "b", "c")
    }

    private fun createDisposable(name: String): Disposable {
        return object : Disposable {
            override fun dispose() {
                addResult(name)
            }
        }
    }
}