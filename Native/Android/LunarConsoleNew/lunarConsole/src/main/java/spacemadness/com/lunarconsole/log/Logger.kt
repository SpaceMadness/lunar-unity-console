package spacemadness.com.lunarconsole.log

/** Interface which represents basic logging operations:
 * - writing log message
 * - writing exception stacktrace
 */
interface Logger {
    fun log(logLevel: LogLevel, message: String)
    fun log(logLevel: LogLevel, throwable: Throwable)
}