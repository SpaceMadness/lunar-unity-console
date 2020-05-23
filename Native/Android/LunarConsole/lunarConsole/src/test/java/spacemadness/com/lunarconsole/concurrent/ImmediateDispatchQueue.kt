package spacemadness.com.lunarconsole.concurrent

class ImmediateDispatchQueue(
        private val dispatchImmediately: Boolean = false
) : DispatchQueue("Immediate Queue") {
    private val tasks = mutableListOf<DispatchTask>()

    override fun isCurrent() = true

    override fun stop() {
    }

    override fun schedule(task: DispatchTask, delay: Long) {
        if (dispatchImmediately) {
            task.execute()
        } else {
            tasks.add(task)
        }
    }

    fun dispatchTasks() {
        tasks.forEach { task -> task.run() }
        tasks.clear()
    }
}