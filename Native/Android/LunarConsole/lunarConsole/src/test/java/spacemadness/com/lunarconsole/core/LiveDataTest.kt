package spacemadness.com.lunarconsole.core

import org.junit.Test

import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.concurrent.ImmediateDispatchQueue

class LiveDataTest : TestCase() {
    @Test
    fun testObservers() {
        val data = MockLiveData("a")
        val disposable1 = data.observe(MockObserver("1"))
        assertResults("1: a")

        val disposable2 = data.observe(MockObserver("2"))
        val disposable3 = data.observe(MockObserver("3"))
        assertResults("2: a", "3: a")

        data.value = "b"
        assertResults("1: b", "2: b", "3: b")

        data.value = "c"
        assertResults("1: c", "2: c", "3: c")

        disposable1.dispose()

        data.value = "d"
        assertResults("2: d", "3: d")

        disposable3.dispose()

        data.value = "e"
        assertResults("2: e")

        disposable2.dispose()

        data.value = "f"
        assertResults()
    }

    internal inner class MockObserver<T>(private val name: String) : Observer<T> {

        override fun onChanged(t: T) {
            addResult("$name: $t")
        }
    }

    internal class MockLiveData<T>(value: T) : MutableLiveData<T>(value, ImmediateDispatchQueue())
}