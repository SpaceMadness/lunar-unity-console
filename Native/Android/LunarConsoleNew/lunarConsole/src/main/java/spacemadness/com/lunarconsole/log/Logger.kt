package spacemadness.com.lunarconsole.log

import android.util.Log.*
import java.io.PrintWriter
import java.io.StringWriter

/** Interface which represents basic logging operations:
 * - writing log message
 * - writing exception stacktrace
 */
interface Logger {
    fun log(logLevel: LogLevel, message: String)
    fun log(logLevel: LogLevel, throwable: Throwable)
}

fun createDefaultLogger(): Logger = DefaultLogger()

private class DefaultLogger : Logger {
    override fun log(logLevel: LogLevel, message: String) {
        println(PRIORITIES[logLevel.ordinal], TAG, message)
    }

    override fun log(logLevel: LogLevel, throwable: Throwable) {
        println(PRIORITIES[logLevel.ordinal], TAG, getStackTrace(throwable))
    }

    companion object {
        private const val TAG = "Lunar Console"

        private val PRIORITIES = Array(LogLevel.values().size) { i ->
            when (i) {
                LogLevel.Debug.ordinal -> DEBUG
                LogLevel.Info.ordinal -> INFO
                LogLevel.Warning.ordinal -> WARN
                LogLevel.Error.ordinal -> ERROR
                LogLevel.Verbose.ordinal -> VERBOSE
                else -> DEBUG
            }
        }

        private fun getStackTrace(throwable: Throwable): String {
            val writer = StringWriter()
            throwable.printStackTrace(PrintWriter(writer))
            return writer.toString()
        }
    }
}