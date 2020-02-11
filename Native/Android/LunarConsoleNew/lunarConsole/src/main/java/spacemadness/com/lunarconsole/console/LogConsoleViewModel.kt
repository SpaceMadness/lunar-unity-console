package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.IntMapper
import spacemadness.com.lunarconsole.reactive.*
import spacemadness.com.lunarconsole.ui.Router
import spacemadness.com.lunarconsole.utils.StringUtils
import kotlin.math.min

class LogConsoleViewModel(
    private val console: LogConsole
) {
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

    // overflow streams
    val overflowStream: Observable<String?> =
        createOverflowStream(console.counterStream, mapper = object : IntMapper<LogCounter> {
            override fun map(counter: LogCounter): Int {
                return counter.overflow
            }
        })

    // toggle lock stream
    private val toggleLockSubject = BehaviorSubject(true)
    val toggleLockStream: Observable<Boolean> = toggleLockSubject

    // toggle lock stream
    private val selectedEntrySubject = PublishSubject<LogEntry>()
    val selectedEntryStream: Observable<LogEntry> = selectedEntrySubject

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

    fun onEntryClick(entry: LogEntry, position: Int) {
        selectedEntrySubject.post(entry)
    }

    fun onEntryLongClick(entry: LogEntry, position: Int) {
    }

    companion object {
        private const val MAX_COUNTER = 999

        // this is done to prevent unnecessary int boxing and code duplication
        // FIXME: explain it better!
        private fun createCounterStream(
            stream: Observable<LogCounter>,
            mapper: IntMapper<LogCounter>
        ) = stream
            // don't count past MAX_COUNTER
            .distinctIntUntilChanged(object : IntMapper<LogCounter> {
                override fun map(counter: LogCounter): Int {
                    return min(mapper(counter), MAX_COUNTER)
                }
            })
            // make it pretty
            .map { counter ->
                StringUtils.toPrettyCounter(
                    min(mapper(counter), MAX_COUNTER),
                    MAX_COUNTER
                )
            }

        private fun createOverflowStream(
            stream: Observable<LogCounter>,
            mapper: IntMapper<LogCounter>
        ) = stream
            // don't count past MAX_COUNTER
            .distinctIntUntilChanged(object : IntMapper<LogCounter> {
                override fun map(counter: LogCounter): Int {
                    return min(mapper(counter), MAX_COUNTER)
                }
            })
            // make it pretty
            .map { counter ->
                val count = min(mapper(counter), MAX_COUNTER)
                if (count > 0) StringUtils.toPrettyCounter(count, MAX_COUNTER) else null
            }
    }
}