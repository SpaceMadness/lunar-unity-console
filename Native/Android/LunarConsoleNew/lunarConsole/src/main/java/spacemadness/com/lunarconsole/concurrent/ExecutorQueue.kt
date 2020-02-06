package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.Disposable

/**
 * An [Executor] object which also provides an ability to cancel all tasks and check if the current
 * code is running on a specific queue.
 * @param name - an optional name for the queue.
 */
abstract class ExecutorQueue(val name: String? = null) : Executor, Disposable {
    /**
     * @returns true, if the code is running on this queue.
     */
    abstract val isCurrent: Boolean

    /**
     * Cancels all pending tasks
     */
    abstract fun cancelAll()
}