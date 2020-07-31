package spacemadness.com.lunarconsole.rx

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.rx.Observables.map
import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction

class ObservableOperatorTest : TestCase() {
    @Test
    fun map() {
        val stream = PublishSubject<Int>()
        val subscription = map(stream, MapFunction { value: Int -> value.toString() })
                .subscribe { value -> addResult(value) }

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