package spacemadness.com.lunarconsole.concurrent

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import spacemadness.com.lunarconsole.core.TimeInterval

internal class SerialExecutorQueue private constructor(
    name: String?,
    private val handler: Handler,
    private val handlerThread: HandlerThread?
) : ExecutorQueue(name) {

    override val isCurrent get() = Looper.myLooper() == handler.looper

    override fun cancelAll() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun execute(task: Runnable, delay: TimeInterval) {
        if (delay > 0) {
            handler.postDelayed(task, delay)
        } else {
            handler.post(task)
        }
    }

    override fun dispose() {
        cancelAll()
        handlerThread?.quit()
    }

    companion object {
        fun create(name: String?): SerialExecutorQueue {
            val handlerThread = HandlerThread(name).apply { start() }
            val handler = Handler(handlerThread.looper)
            return SerialExecutorQueue(name, handler, handlerThread)
        }

        fun create(looper: Looper, name: String?): SerialExecutorQueue {
            val handler = Handler(looper)
            return SerialExecutorQueue(name, handler, null)
        }
    }
}