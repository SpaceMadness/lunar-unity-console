package spacemadness.com.lunarconsole.console

class LogConsole(capacity: Int, trimSize: Int) {
    private val entries = LogEntryList(capacity, trimSize)
    val itemCount get() = entries.count()

    var callback: Callback? = null

    private val diff = LogEntryList.Diff()

    //region Messages

    fun logMessages(messages: List<LogEntry>) {
        entries.addAll(messages, diff)
        callback?.onUpdate(this, diff)
    }

    operator fun get(position: Int) = entries[position]

    //endregion

    interface Callback {
        fun onUpdate(console: LogConsole, diff: LogEntryList.Diff)
    }
}