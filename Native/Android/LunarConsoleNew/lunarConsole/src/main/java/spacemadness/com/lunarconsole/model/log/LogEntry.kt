package spacemadness.com.lunarconsole.model.log

import spacemadness.com.lunarconsole.model.Entry

enum class LogEntryType {
    ERROR, ASSERT, WARNING, LOG, EXCEPTION
}

/** Represents a single console log entry */
open class LogEntry(
    var index: Int, // FIXME: make immutable
    val type: LogEntryType,
    val message: String,
    val stackTrace: String? = null
) : Entry {
    val hasStackTrace = !stackTrace.isNullOrEmpty()

    init {
        require(index >= 0) { "Invalid index: $index" }
    }

    override fun toString(): String {
        return "index=$index type=$type message=\"$message\" stackTrace=$stackTrace"
    }
}

val LogEntryType.isErrorType: Boolean
    get() {
        return this == LogEntryType.EXCEPTION || this == LogEntryType.ERROR || this == LogEntryType.ASSERT
    }

fun getMask(type: LogEntryType): Int {
    return 1 shl type.ordinal
}