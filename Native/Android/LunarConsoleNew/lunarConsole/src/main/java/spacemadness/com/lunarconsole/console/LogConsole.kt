package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.Observable
import spacemadness.com.lunarconsole.core.PublishSubject

class LogConsole(capacity: Int, trimSize: Int) {
    private val entries = LogEntryList(capacity, trimSize)
    private val diff = LogEntryList.Diff()
    private val diffSubject = PublishSubject<LogEntryList.Diff>()

    val itemCount get() = entries.count()
    val diffStream: Observable<LogEntryList.Diff> = diffSubject

    //region Messages

    fun logMessages(messages: List<LogEntry>) {
        entries.addAll(messages, diff)
        diffSubject.post(diff)
    }

    operator fun get(position: Int) = entries[position]

    //endregion
}