package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.TimeInterval

class ImmediateExecutorQueue(
    name: String? = null,
    private val dispatchImmediately: Boolean = true
) : ExecutorQueue(name ?: "immediate") {
    override val isCurrent = true

    var onTaskSchedule: (task: Runnable) -> Unit = {}
    var onTaskDispatch: (task: Runnable) -> Unit = {}

    private val tasks = mutableListOf<Runnable>()

    fun dispatch() {
        for (task in tasks) {
            dispatch(task)
        }
    }

    private fun dispatch(task: Runnable) {
        task.run()
        onTaskDispatch.invoke(task)
    }

    override fun cancelAll() {
        tasks.clear()
    }

    override fun execute(task: Runnable, delay: TimeInterval) {
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