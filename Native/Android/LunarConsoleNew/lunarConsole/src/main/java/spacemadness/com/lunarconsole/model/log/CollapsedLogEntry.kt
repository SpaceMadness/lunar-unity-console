package spacemadness.com.lunarconsole.model.log

class CollapsedLogEntry(
    index: Int,
    type: LogEntryType,
    message: String,
    stackTrace: String? = null
) : LogEntry(index, type, message, stackTrace) {
    var count: Int = 1
        private set

    fun increaseCount() {
        count += 1
    }

    override fun toString(): String {
        return "${super.toString()} count=$count"
    }
}

fun LogEntry.asCollapsedEntry() = CollapsedLogEntry(index, type, message, stackTrace)