package spacemadness.com.lunarconsole.rx

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.rx.Observables.combineLatest
import spacemadness.com.lunarconsole.rx.Observables.map
import spacemadness.com.lunarconsole.utils.CollectionUtils.MapFunction
import spacemadness.com.lunarconsole.utils.StringUtils

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

    @Test
    fun combineLatest() {
        val stream1 = PublishSubject<String>()
        val stream2 = PublishSubject<String>();
        val subscription = combineLatest(stream1, stream2)
                .subscribe { list -> addResult(StringUtils.Join(list, "")) }

        assertResults()

        stream1.post("A")
        assertResults()

        stream1.post("B")
        assertResults()

        stream2.post("1")
        assertResults("B1")

        stream2.post("2")
        assertResults("B2")

        stream1.post("C")
        assertResults("C2")

        stream2.post("3")
        assertResults("C3")

        stream1.post("D")
        assertResults("D3")

        subscription.dispose()

        stream1.post("E")
        stream2.post("4")
        assertResults()
    }
}