package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.TimeInterval

/**
 * An object that executes submitted runnable tasks. This
 * interface provides a way of decoupling task submission from the
 * mechanics of how each task will be run, including details of thread
 * use, scheduling, etc.
 */
interface Executor {
    /**
     * Schedules a task for the execution.
     * @param task - runnable task for the execution.
     * @param delay - [TimeInterval] delay for the execution.
     */
    fun execute(delay: TimeInterval, task: () -> Unit)

    fun execute(task: () -> Unit) = execute(0, task)
}