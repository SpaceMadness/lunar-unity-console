package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.core.BehaviorSubject
import spacemadness.com.lunarconsole.core.Observable
import spacemadness.com.lunarconsole.core.PublishSubject

class LogConsole(capacity: Int, trimSize: Int) {
    private val entries = LogEntryList(capacity, trimSize)
    private val reusableDiff = LogEntryList.Diff()
    private val reusableCounter = LogCounter()

    private val diffSubject = PublishSubject<LogEntryList.Diff>()
    private val counterSubject = BehaviorSubject(reusableCounter)

    val itemCount get() = entries.count()
    val diffStream: Observable<LogEntryList.Diff> = diffSubject
    val counterStream: Observable<LogCounter> = counterSubject

    //region Messages

    fun add(messages: List<LogEntry>) {
        entries.addAll(messages, reusableDiff)

        // post diff
        diffSubject.post(reusableDiff)

        // post counter
        reusableCounter.log = entries.logCount
        reusableCounter.warn = entries.warningCount
        reusableCounter.error = entries.errorCount
        counterSubject.value = reusableCounter
    }

    fun getEntry(position: Int) = entries[position]

    //endregion
}

data class LogCounter(var log: Int = 0, var warn: Int = 0, var error: Int = 0)