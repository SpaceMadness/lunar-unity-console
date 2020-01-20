package spacemadness.com.lunarconsole.model

import org.junit.Test

import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.concurrent.ImmediateExecutorQueue
import spacemadness.com.lunarconsole.console.LogEntry
import spacemadness.com.lunarconsole.console.LogEntryBuffer
import spacemadness.com.lunarconsole.console.LogEntryType

class LogEntryBufferTest : TestCase() {
    @Test
    fun add() {
        val executor = ImmediateExecutorQueue(dispatchImmediately = false).apply {
            onTaskSchedule = { addResult("scheduled") }
        }
        val buffer =
            LogEntryBuffer(executor = executor) { entries ->
                entries.forEach { addResult(it.message) }
            }

        buffer.add("1")
        assertResults("scheduled")

        buffer.add("2")
        buffer.add("3")
        assertResults()

        executor.dispatch()
        assertResults("1", "2", "3")

        buffer.add("4")
        assertResults("scheduled")

        buffer.add("5")
        buffer.add("6")
        assertResults()

        executor.dispatch()
        assertResults("4", "5", "6")
    }
}

private fun LogEntryBuffer.add(message: String) = add(
    LogEntry(
        index = 0,
        type = LogEntryType.LOG,
        message = message,
        stackTrace = null
    )
)