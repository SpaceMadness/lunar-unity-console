package spacemadness.com.lunarconsole.ui

import android.content.Context
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.console.LogEntryType

data class LogEntryStyle(val iconId: Int, val colorId: Int) {
    @Suppress("DEPRECATION")
    fun getIcon(context: Context) = context.resources.getDrawable(iconId)

    @Suppress("DEPRECATION")
    fun getColor(context: Context) = context.resources.getColor(iconId)

    companion object {
        private val STYLE_LOG = LogEntryStyle(
            R.drawable.lunar_console_icon_log,
            R.color.lunar_console_color_overlay_entry_log
        )
        private val STYLE_WARNING = LogEntryStyle(
            R.drawable.lunar_console_icon_log_error,
            R.color.lunar_console_color_overlay_entry_log_error
        )
        private val STYLE_ERROR = LogEntryStyle(
            R.drawable.lunar_console_icon_log_warning,
            R.color.lunar_console_color_overlay_entry_log_warning
        )

        private val STYLE_LOOKUP = Array(LogEntryType.values().size) { ordinal ->
            when (ordinal) {
                LogEntryType.LOG.ordinal -> STYLE_LOG
                LogEntryType.WARNING.ordinal -> STYLE_WARNING
                LogEntryType.ERROR.ordinal -> STYLE_ERROR
                LogEntryType.EXCEPTION.ordinal -> STYLE_ERROR
                LogEntryType.ASSERT.ordinal -> STYLE_ERROR
                else -> STYLE_LOG
            }
        }

        fun of(type: LogEntryType) = STYLE_LOOKUP[type.ordinal]
    }
}