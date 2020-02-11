package spacemadness.com.lunarconsole.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.console.LogEntry
import spacemadness.com.lunarconsole.utils.StackTrace

interface Router {
    fun showLogDetails(entry: LogEntry)
}

class DefaultRouter(private val context: Context) : Router {
    // TODO: refactor this code
    override fun showLogDetails(entry: LogEntry) {
        val builder = AlertDialog.Builder(context)

        val inflater = LayoutInflater.from(context)
        val contentView =
            inflater.inflate(R.layout.lunar_console_layout_log_details_dialog, null, false)
        val messageView = contentView.findViewById<TextView>(R.id.lunar_console_log_details_message)
        val stacktraceView =
            contentView.findViewById<TextView>(R.id.lunar_console_log_details_stacktrace)

        val message = entry.message
        val stackTrace = if (entry.hasStackTrace) StackTrace.optimize(entry.stackTrace)
        else context.resources.getString(R.string.lunar_console_log_details_dialog_no_stacktrace_warning)

        val style = LogEntryStyle.of(entry.type)

        messageView.text = message
        messageView.setDrawables(left = style.getIcon(context))
        stacktraceView.text = stackTrace

        builder.setView(contentView)
        builder.setPositiveButton(R.string.lunar_console_log_details_dialog_button_copy_to_clipboard) { _, _ ->
            var text = message
            if (entry.hasStackTrace) {
                text += "\n\n" + stackTrace
            }
            copyToClipboard(text)
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        private fun copyToClipboard(text: String) {
            TODO("Implement me")
        }
    }
}