package spacemadness.com.lunarconsole.concurrent

import android.os.Looper

/**
 * Interface responsible for executor queues creation
 */
interface ExecutorQueueFactory {
    /**
     * Creates a serial 'main' queue for working with UI.
     */
    fun createMainQueue(): ExecutorQueue

    /**
     * Creates a background serial queue.
     * @param name - optional name for the queue.
     */
    fun createSerialQueue(name: String? = null): ExecutorQueue
}

fun createDefaultExecutorQueueFactory(): ExecutorQueueFactory = DefaultExecutorQueueFactory()

/**
 * Default [ExecutorQueueFactory] factory implementation.
 */
private class DefaultExecutorQueueFactory : ExecutorQueueFactory {
    override fun createMainQueue() = SerialExecutorQueue.create(Looper.getMainLooper(), "Main")
    override fun createSerialQueue(name: String?) = SerialExecutorQueue.create(name)
}