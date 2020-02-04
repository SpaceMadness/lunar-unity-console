package spacemadness.com.lunarconsole

import spacemadness.com.lunarconsole.concurrent.Executors
import spacemadness.com.lunarconsole.console.LogEntryBuffer
import spacemadness.com.lunarconsole.console.LogEntry

class Plugin(executors: Executors) {
    private val logEntryBuffer =
        LogEntryBuffer(executors.state) { entries ->
            handleLogEntries(entries)
        }

    //region Public Interface

    fun start() {
        TODO()
    }

    fun logMessage(message: String, stackTrace: String?, logType: Int) {
        TODO()
    }

    fun showConsole() {
        TODO()
    }

    fun hideConsole() {
        TODO()
    }

    fun showOverlay() {
        TODO()
    }

    fun hideOverlay() {
        TODO()
    }

    fun clearConsole() {
        TODO()
    }

    fun registerAction(actionId: Int, actionName: String) {
        TODO()
    }

    fun unregisterAction(actionId: Int) {
        TODO()
    }

    fun registerVariable(
        variableId: Int,
        name: String,
        type: String,
        value: String?,
        defaultValue: String?,
        flags: Int,
        hasRange: Boolean,
        rangeMin: Float,
        rangeMax: Float
    ) {
        TODO()
    }

    fun updateVariable(variableId: Int, value: String?) {
        TODO()
    }

    fun destroy() {
        TODO()
    }

    //endregion

    //region Log Entries

    private fun handleLogEntries(entries: List<LogEntry>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //endregion
}