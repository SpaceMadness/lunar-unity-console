package spacemadness.com.lunarconsole.model

enum class LogEntryType {
    ERROR, ASSERT, WARNING, LOG, EXCEPTION
}

class LogEntry(
    val index: Int,
    val type: LogEntryType,
    val message: String,
    val stacktrace: String?
) : Entry {
    init {
        require(index >= 0) { "Invalid index: $index" }
    }
}


