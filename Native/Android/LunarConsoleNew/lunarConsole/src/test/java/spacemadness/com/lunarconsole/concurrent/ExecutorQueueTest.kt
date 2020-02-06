package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.TimeInterval

typealias Task = () -> Unit

class ImmediateExecutorQueue(
    name: String? = null,
    private val dispatchImmediately: Boolean = true
) : ExecutorQueue(name ?: "immediate") {
    override val isCurrent = true

    var onTaskSchedule: (task: Task) -> Unit = {}
    var onTaskDispatch: (task: Task) -> Unit = {}

    private val tasks = mutableListOf<Task>()

    fun dispatch() {
        for (task in tasks) {
            dispatch(task)
        }
    }

    private fun dispatch(task: Task) {
        task.invoke()
        onTaskDispatch.invoke(task)
    }

    override fun cancelAll() {
        tasks.clear()
    }

    override fun execute(delay: TimeInterval, task: Task) {
        if (dispatchImmediately) {
            dispatch(task)
        } else {
            onTaskSchedule.invoke(task)
            tasks.add(task)
        }
    }

    override fun dispose() {
        cancelAll()
    }
}