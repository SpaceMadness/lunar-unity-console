package spacemadness.com.lunarconsole.console

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import spacemadness.com.lunarconsole.R

class LogConsoleView(
    context: Context,
    private val viewModel: LogConsoleViewModel
) : LinearLayout(context) {
    init {
        inflate(context, R.layout.lunar_console_layout_console_log_view, this)
    }
}