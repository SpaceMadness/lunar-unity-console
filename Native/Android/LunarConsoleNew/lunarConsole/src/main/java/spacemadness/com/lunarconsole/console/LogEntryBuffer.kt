package spacemadness.com.lunarconsole.console

import spacemadness.com.lunarconsole.concurrent.Executor

class LogEntryBuffer(
    private val executor: Executor,
    private val callback: (List<LogEntry>) -> Unit
) {
    private val queue = mutableListOf<LogEntry>()

    private val task = {
        synchronized(queue) {
            callback(queue)
            queue.clear()
        }
    }

    fun add(entry: LogEntry) {
        synchronized(queue) {
            queue.add(entry)
            if (queue.size == 1) {
                executor.execute(task)
            }
        }
    }
}