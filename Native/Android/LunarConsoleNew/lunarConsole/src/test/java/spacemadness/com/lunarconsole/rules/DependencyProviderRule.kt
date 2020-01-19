package spacemadness.com.lunarconsole.rules

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import spacemadness.com.lunarconsole.concurrent.ExecutorQueueFactory
import spacemadness.com.lunarconsole.concurrent.ImmediateExecutorQueue
import spacemadness.com.lunarconsole.di.DependencyProvider
import spacemadness.com.lunarconsole.log.LogLevel
import spacemadness.com.lunarconsole.log.Logger

class DependencyProviderRule(private val enableConsoleOutput: Boolean = false) : TestWatcher() {
    override fun starting(description: Description?) {
        DependencyProvider.register(createPlatformLogger(enableConsoleOutput))
        DependencyProvider.register(createExecutionQueueFactory())
    }

    override fun finished(description: Description?) {
        DependencyProvider.clear()
    }
}

private fun createExecutionQueueFactory(): ExecutorQueueFactory = MockExecutorFactory
private fun createPlatformLogger(enableOutput: Boolean) = if (enableOutput) MockLogger else NullLogger

private object MockExecutorFactory : ExecutorQueueFactory {
    override fun createMainQueue() =
        ImmediateExecutorQueue("main")

    override fun createSerialQueue(name: String?) =
        ImmediateExecutorQueue(name)
}

private object NullLogger : Logger {
    override fun log(logLevel: LogLevel, message: String) {
    }

    override fun log(logLevel: LogLevel, throwable: Throwable) {
    }
}

private object MockLogger : Logger {
    override fun log(logLevel: LogLevel, message: String) {
        println(message)
    }

    override fun log(logLevel: LogLevel, throwable: Throwable) {
        throwable.printStackTrace()
    }
}