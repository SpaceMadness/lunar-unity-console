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