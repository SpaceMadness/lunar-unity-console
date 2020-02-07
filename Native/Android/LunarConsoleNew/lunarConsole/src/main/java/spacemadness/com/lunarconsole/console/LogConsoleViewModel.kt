package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.BehaviorSubject
import spacemadness.com.lunarconsole.core.Observable

class LogConsoleViewModel(private val console: LogConsole) {
    val dataSource = object : DataSource<LogEntry> {
        override fun getItemCount() = console.itemCount
        override fun getItem(position: Int) = console.getEntry(position)
    }

    val diffStream: Observable<LogEntryList.Diff> = console.diffStream
    val counterStream: Observable<LogCounter> = console.counterStream

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
}