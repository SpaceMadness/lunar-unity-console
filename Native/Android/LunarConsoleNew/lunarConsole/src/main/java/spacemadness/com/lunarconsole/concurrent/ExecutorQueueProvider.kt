package spacemadness.com.lunarconsole.concurrent

import android.os.Looper
import spacemadness.com.lunarconsole.di.Provider

interface ExecutorQueueProvider : Provider {
    fun createMainQueue(): ExecutorQueue
    fun createQueue(type: ExecutorQueueType, name: String? = null): ExecutorQueue
}

internal class DefaultExecutorQueueProvider : ExecutorQueueProvider {
    override fun createMainQueue() =
        SerialExecutorQueue.create(Looper.getMainLooper(), "Main")

    override fun createQueue(type: ExecutorQueueType, name: String?) = when (type) {
        ExecutorQueueType.Serial -> SerialExecutorQueue.create(name)
        ExecutorQueueType.Concurrent -> TODO("Implement concurrent queue")
    }
}