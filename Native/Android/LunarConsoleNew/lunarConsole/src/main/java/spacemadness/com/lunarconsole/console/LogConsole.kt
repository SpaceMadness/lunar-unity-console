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

        // diff
        postDiff()

        // counter
        postCounter()
    }

    fun getEntry(position: Int) = entries[position]

    fun clear() {
        val oldSize = entries.count()
        entries.clear()

        // diff
        reusableDiff.trimCount = oldSize
        postDiff()

        // counter
        postCounter()
    }

    //endregion

    //region Helpers

    private fun postDiff() {
        diffSubject.post(reusableDiff)
        reusableDiff.clear()
    }

    private fun postCounter() {
        reusableCounter.log = entries.logCount
        reusableCounter.warn = entries.warningCount
        reusableCounter.error = entries.errorCount
        counterSubject.value = reusableCounter
    }

    //endregion
}

data class LogCounter(var log: Int = 0, var warn: Int = 0, var error: Int = 0)