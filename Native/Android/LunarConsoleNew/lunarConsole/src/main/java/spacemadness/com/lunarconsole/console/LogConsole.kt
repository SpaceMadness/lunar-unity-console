package spacemadness.com.lunarconsole.console

class LogConsole(capacity: Int, trimSize: Int) {
    private val entries = LogEntryList(capacity, trimSize)

    //region Messages

    fun logMessages(messages: List<LogEntry>) {
        val diff = LogEntryList.Diff()
        entries.addAll(messages, diff)

    }

    //endregion

    interface Callback {
        fun onUpdate(console: LogConsole, diff: LogEntryList.Diff)
    }
}