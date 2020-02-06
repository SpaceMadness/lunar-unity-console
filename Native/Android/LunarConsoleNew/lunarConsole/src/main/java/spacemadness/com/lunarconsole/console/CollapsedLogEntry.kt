package spacemadness.com.lunarconsole.console

class CollapsedLogEntry(
    var position: Int, // TODO: make immutable
    type: LogEntryType,
    message: String,
    stackTrace: String? = null
) : LogEntry(type, message, stackTrace) {
    var count: Int = 1
        private set

    fun increaseCount() {
        count += 1
    }

    override fun toString(): String {
        return "${super.toString()} count=$count"
    }
}

fun LogEntry.asCollapsedEntry() =
    CollapsedLogEntry(
        position = -1,
        type = type,
        message = message,
        stackTrace = stackTrace
    )