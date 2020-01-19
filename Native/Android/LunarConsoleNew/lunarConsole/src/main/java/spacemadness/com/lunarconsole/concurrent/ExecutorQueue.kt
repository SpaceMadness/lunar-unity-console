package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.Destroyable
import spacemadness.com.lunarconsole.di.Providers

enum class ExecutorQueueType {
    Serial, Concurrent
}

abstract class ExecutorQueue(val name: String? = null) : Executor, Destroyable {
    abstract val isCurrent: Boolean

    abstract fun cancelAll()

    companion object {
        private val provider = Providers.of<ExecutorQueueProvider>()
        val main = provider.createMainQueue()
        val state = provider.createQueue(ExecutorQueueType.Serial, "Lunar Console")
    }
}