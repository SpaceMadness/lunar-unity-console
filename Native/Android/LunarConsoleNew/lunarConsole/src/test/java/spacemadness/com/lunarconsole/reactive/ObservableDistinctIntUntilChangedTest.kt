package spacemadness.com.lunarconsole.reactive

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.core.IntMapper
import java.lang.Integer.min

class ObservableDistinctIntUntilChangedTest : TestCase() {
    @Test
    fun testDistinct() {
        val stream = PublishSubject<IntWrapper>()
        val subscription = stream
            .distinctIntUntilChanged(object : IntMapper<IntWrapper> {
                override fun map(wrapper: IntWrapper): Int {
                    return wrapper.value
                }
            }).subscribe {
                addResult(it.value)
            }

        stream.post(1, 1, 2, 1, 1, 3, 3, 1)
        assertResults(1, 2, 1, 3, 1)

        subscription.dispose()

        stream.post(1, 2, 3, 4)
        assertResults()
    }

    @Test
    fun testDistinctWithTransform() {
        val stream = PublishSubject<IntWrapper>()
        stream
            .distinctIntUntilChanged(object : IntMapper<IntWrapper> {
            override fun map(wrapper: IntWrapper): Int {
                return min(2, wrapper.value)
            }
        }).subscribe {
            addResult(it.value)
        }

        stream.post(1, 1, 2, 3, 4, 5, 2, 1)
        assertResults(1, 2, 1)
    }
}

private fun PublishSubject<IntWrapper>.post(vararg values: Int) =
    values.forEach { post(IntWrapper(it)) }

private data class IntWrapper(val value: Int) {
    override fun toString() = value.toString()
}