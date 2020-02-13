package spacemadness.com.lunarconsole.app

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import spacemadness.com.lunarconsole.actions.*
import spacemadness.com.lunarconsole.concurrent.ExecutorQueueFactory
import spacemadness.com.lunarconsole.console.LogConsole
import spacemadness.com.lunarconsole.console.LogEntry
import spacemadness.com.lunarconsole.console.LogEntryType
import spacemadness.com.lunarconsole.di.DependencyProvider

class MainActivity : AppCompatActivity() {
    private val queue = DependencyProvider.of<ExecutorQueueFactory>().createMainQueue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val console = LogConsole(capacity = 65536, trimSize = 512)
//        // console.collapsed = true
//        console.add(Array(65537) {
//            LogEntry(LogEntryType.LOG, "Message-$it", null)
//        }.asList())
//
//        val router = DefaultRouter()
//        val consoleViewModel = LogConsoleViewModel(console)
//        val consoleView = LogConsoleView(this, consoleViewModel, router)
//        containerView.addView(consoleView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        val actions = ActionRegistry()
        actions.register(Action(0, "Action-0", "Group-0"))
        actions.register(Action(1, "Action-1", "Group-1"))
        actions.register(Action(2, "Action-2", "Group-2"))
        actions.register(Action(3, "Action-3", "Group-3"))

        val variables = VariableRegistry()
        variables.register(StringVariable(0,"String", "value", "default value"))
        variables.register(BooleanVariable(1,"Boolean", true, false))
        variables.register(IntVariable(2,"Int", 0, 0))
        variables.register(FloatVariable(3,"Int", 0.0f, 0.0f))
        variables.register(EnumVariable(4,"Enum", "One", "Two", values = listOf("One", "Two", "Three")))

        val viewModel = ActionsViewModel(actions, variables)
        val actionsView = ActionsView(this, viewModel)
        containerView.addView(actionsView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

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