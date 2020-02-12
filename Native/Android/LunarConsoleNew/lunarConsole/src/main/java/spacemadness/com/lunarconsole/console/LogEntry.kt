package spacemadness.com.lunarconsole.console

enum class LogEntryType {
    ERROR, ASSERT, WARNING, LOG, EXCEPTION
}

/** Represents a single console log entry */
open class LogEntry(
    val type: LogEntryType,
    val message: String,
    val stackTrace: String? = null
) {
    val hasStackTrace = !stackTrace.isNullOrEmpty()

    override fun toString(): String {
        return "type=$type message=\"$message\" stackTrace=$stackTrace"
    }

    // FIXME: inline this
    fun hasStackTrace() = hasStackTrace
}

val LogEntryType.isErrorType: Boolean
    get() {
        return this == LogEntryType.EXCEPTION || this == LogEntryType.ERROR || this == LogEntryType.ASSERT
    }

fun getMask(type: LogEntryType): Int {
    return 1 shl type.ordinal
}