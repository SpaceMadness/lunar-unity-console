package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.TimeInterval

interface Executor {
    fun execute(task: Runnable, delay: TimeInterval = 0)
}