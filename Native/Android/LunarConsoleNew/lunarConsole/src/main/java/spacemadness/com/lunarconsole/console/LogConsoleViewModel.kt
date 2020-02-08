package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.IntMapper
import spacemadness.com.lunarconsole.reactive.BehaviorSubject
import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.reactive.distinctIntUntilChanged
import spacemadness.com.lunarconsole.reactive.map
import spacemadness.com.lunarconsole.utils.StringUtils
import kotlin.math.min

class LogConsoleViewModel(private val console: LogConsole) {
    val dataSource = object : DataSource<LogEntry> {
        override fun getItemCount() = console.itemCount
        override fun getItem(position: Int) = console.getEntry(position)
    }

    val diffStream: Observable<LogEntryList.Diff> = console.diffStream

    // counter streams
    val logCounterStream =
        createCounterStream(console.counterStream, mapper = object : IntMapper<LogCounter> {
            override fun map(counter: LogCounter): Int {
                return counter.log
            }
        })
    val warnCounterStream =
        createCounterStream(console.counterStream, mapper = object : IntMapper<LogCounter> {
            override fun map(counter: LogCounter): Int {
                return counter.warn
            }
        })
    val errorCounterStream =
        createCounterStream(console.counterStream, mapper = object : IntMapper<LogCounter> {
            override fun map(counter: LogCounter): Int {
                return counter.error
            }
        })

    private val toggleLockSubject = BehaviorSubject(false)
    val toggleLockStream: Observable<Boolean> = toggleLockSubject

    fun clearLogs() {
        console.clear()
    }

    fun toggleLock() {
        toggleLockSubject.value = !toggleLockSubject.value
    }

    fun copyLogs() {
        TODO("Implement me")
    }

    fun emailLogs() {
        TODO("Implement me")
    }

    companion object {
        private const val MAX_COUNTER = 999

        // this is done to prevent unnecessary int boxing and code duplication
        private fun createCounterStream(
            stream: Observable<LogCounter>,
            mapper: IntMapper<LogCounter>
        ) = stream
            // don't count past MAX_COUNTER
            .distinctIntUntilChanged(object : IntMapper<LogCounter> {
                override fun map(counter: LogCounter): Int {
                    return min(MAX_COUNTER, mapper(counter))
                }
            })
            // make it pretty
            .map { counter -> StringUtils.toPrettyCounter(mapper(counter), MAX_COUNTER) }
    }
}