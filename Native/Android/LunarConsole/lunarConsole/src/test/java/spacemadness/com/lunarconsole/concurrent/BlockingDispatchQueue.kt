package spacemadness.com.lunarconsole.concurrent

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

internal class BlockingDispatchQueue(name: String) : DispatchQueue(name) {
    private val threadGroup: ThreadGroup = ThreadGroup(name)
    private val executor: ExecutorService

    init {
        executor = Executors.newFixedThreadPool(1) { runnable -> Thread(threadGroup, runnable) }
    }

    override fun dispatch(task: Runnable, delay: Long) {
        // there must be a better way
        val mutex = Object()
        val waiting = AtomicBoolean(true)

        executor.execute {
            synchronized(mutex) {
                try {
                    task.run()
                } finally {
                    waiting.set(false)
                    mutex.notifyAll()
                }
            }
        }

        synchronized(mutex) {
            while (waiting.get()) {
                mutex.wait()

            }
        }
    }

    override fun stop() {
        executor.shutdown()
    }

    override fun isCurrent(): Boolean {
        return Thread.currentThread().threadGroup === threadGroup
    }
}
