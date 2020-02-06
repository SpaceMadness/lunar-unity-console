package spacemadness.com.lunarconsole.app

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import spacemadness.com.lunarconsole.concurrent.ExecutorQueueFactory
import spacemadness.com.lunarconsole.console.*
import spacemadness.com.lunarconsole.di.DependencyProvider

class MainActivity : AppCompatActivity() {
    private val queue = DependencyProvider.of<ExecutorQueueFactory>().createMainQueue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val console = LogConsole(capacity = 6, trimSize = 3)
        console.logMessages(
            LogEntry(LogEntryType.LOG, "Log", null),
            LogEntry(LogEntryType.WARNING, "Warning", null),
            LogEntry(LogEntryType.ERROR, "Error", null)
        )

        val consoleViewModel = LogConsoleViewModel(console)
        val consoleView = LogConsoleView(this, consoleViewModel)
        containerView.addView(consoleView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        logMessage(console, index = 1)
    }

    private fun logMessage(console: LogConsole, index: Int) {
        queue.execute(1000) {
            var pos = index
            console.logMessages(LogEntry(LogEntryType.LOG, "Log-${++pos}", null))
            console.logMessages(LogEntry(LogEntryType.LOG, "Log-${++pos}", null))
            logMessage(console, pos)
        }
    }
}

private fun LogConsole.logMessages(vararg messages: LogEntry) {
    add(messages.asList())
}