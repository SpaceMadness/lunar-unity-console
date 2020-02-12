package spacemadness.com.lunarconsole.app

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import spacemadness.com.lunarconsole.concurrent.ExecutorQueueFactory
import spacemadness.com.lunarconsole.console.*
import spacemadness.com.lunarconsole.di.DependencyProvider
import spacemadness.com.lunarconsole.ui.DefaultRouter

class MainActivity : AppCompatActivity() {
    private val queue = DependencyProvider.of<ExecutorQueueFactory>().createMainQueue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val console = LogConsole(capacity = 65536, trimSize = 512)
        // console.collapsed = true
        console.add(Array(65537) {
            LogEntry(LogEntryType.LOG, "Message-$it", null)
        }.asList())

        val router = DefaultRouter()
        val consoleViewModel = LogConsoleViewModel(console)
        val consoleView = LogConsoleView(this, consoleViewModel, router)
        containerView.addView(consoleView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        //logMessage(console)
    }

    private fun logMessage(console: LogConsole, index: Int = 0) {
        queue.execute(250) {
            console.logMessages(LogEntry(LogEntryType.LOG, "Log-${index + 1}", null))
            logMessage(console, index + 1)
        }
    }
}

private fun LogConsole.logMessages(vararg messages: LogEntry) {
    add(messages.asList())
}