package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.Destroyable
import spacemadness.com.lunarconsole.di.DependencyProvider

abstract class ExecutorQueue(val name: String? = null) : Executor, Destroyable {
    abstract val isCurrent: Boolean

    abstract fun cancelAll()

    companion object {
        private val provider = DependencyProvider.of<ExecutorQueueFactory>()
        val main = provider.createMainQueue()
        val state = provider.createSerialQueue("Lunar Console")
    }
}