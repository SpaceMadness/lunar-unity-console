package spacemadness.com.lunarconsole.utils

import spacemadness.com.lunarconsole.log.Log

object StackTrace {
    const val MARKER_AT = " (at "
    const val MARKER_ASSETS = "/Assets/"

    fun optimize(stackTrace: String?): String? {
        try {
            if (!stackTrace.isNullOrEmpty()) {
                return stackTrace
                    .split("\n")
                    .joinToString("\n") { optimizeLine(it) }
            }
        } catch (e: Exception) {
            Log.e(e, "Error while optimizing stacktrace: $stackTrace")
        }
        return stackTrace
    }

    private fun optimizeLine(line: String): String {
        val start = line.indexOf(MARKER_AT)
        if (start == -1) return line
        val end = line.lastIndexOf(MARKER_ASSETS)
        return if (end == -1) line else line.substring(0, start + MARKER_AT.length) + line.substring(end + 1)
    }
}
