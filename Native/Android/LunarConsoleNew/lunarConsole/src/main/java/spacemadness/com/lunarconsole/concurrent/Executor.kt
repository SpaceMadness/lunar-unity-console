package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.core.TimeInterval

interface Executor {
    fun execute(callback: () -> Unit, delay: TimeInterval)
}