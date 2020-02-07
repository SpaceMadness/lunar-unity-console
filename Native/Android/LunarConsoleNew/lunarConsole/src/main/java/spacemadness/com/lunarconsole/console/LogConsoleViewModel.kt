package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.Observable

class LogConsoleViewModel(private val console: LogConsole) {
    val dataSource = object : DataSource<LogEntry> {
        override fun getItemCount() = console.itemCount
        override fun getItem(position: Int) = console.getEntry(position)
    }

    val diffStream: Observable<LogEntryList.Diff> = console.diffStream
    val counterStream: Observable<LogCounter> = console.counterStream

    fun clearLogs() {
        console.clear()
    }

    fun toggleLock() {
    }

    fun copyLogs() {
    }

    fun emailLogs() {
    }
}