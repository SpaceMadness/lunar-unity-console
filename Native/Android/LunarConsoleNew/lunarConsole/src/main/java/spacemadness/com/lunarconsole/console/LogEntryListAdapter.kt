package spacemadness.com.lunarconsole.console

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import spacemadness.com.lunarconsole.R

class LogEntryListAdapter(
    private val dataSource: DataSource<LogEntry>
) : RecyclerView.Adapter<LogEntryListAdapter.ViewHolder>() {
    var onClickListener: ((entry: LogEntry, position: Int) -> Unit)? = null
    var onLongClickListener: ((entry: LogEntry, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.lunar_console_layout_console_log_entry, parent, false)
        return ViewHolder(
            itemView = itemView,
            onClickListener = ::onClick,
            onLongClickListener = ::onLongClick
        )
    }

    override fun getItemCount(): Int = dataSource.getItemCount()

    fun getItem(position: Int) = dataSource.getItem(position)

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private fun onClick(position: Int) {
        onClickListener?.invoke(getItem(position), position)
    }

    private fun onLongClick(position: Int): Boolean {
        onLongClickListener?.invoke(getItem(position), position)
        return onLongClickListener != null
    }

    class ViewHolder(
        itemView: View,
        private val onClickListener: (position: Int) -> Unit,
        private val onLongClickListener: (position: Int) -> Boolean
    ) : RecyclerView.ViewHolder(itemView) {
        private val layout: View = itemView.findViewById(R.id.lunar_console_log_entry_layout)
        private val messageView: TextView =
            itemView.findViewById(R.id.lunar_console_log_entry_message)
        private val collapsedCountView: TextView =
            itemView.findViewById(R.id.lunar_console_log_collapsed_count)
        private val context = itemView.context

        init {
            itemView.setOnClickListener {
                onClickListener(adapterPosition)
            }
            itemView.setOnLongClickListener {
                onLongClickListener(adapterPosition)
            }
        }

        fun bind(entry: LogEntry, position: Int) {
            layout.setBackgroundColor(getBackgroundColor(context, position))
            messageView.setCompoundDrawablesWithIntrinsicBounds(getIcon(context, entry.type), null, null, null)
            messageView.text = entry.message

            if (entry is CollapsedLogEntry && entry.count > 1) {
                collapsedCountView.visibility = View.VISIBLE
                collapsedCountView.text = entry.count.toString()
            } else {
                collapsedCountView.visibility = View.GONE
            }
        }

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

            private fun getBackgroundColor(context: Context, position: Int): Int {
                val colorId = if (position % 2 == 0)
                    R.color.lunar_console_color_cell_background_dark else
                    R.color.lunar_console_color_cell_background_light

                @Suppress("DEPRECATION")
                return context.resources.getColor(colorId)
            }

            private fun getIcon(context: Context, type: LogEntryType): Drawable? {
                @Suppress("DEPRECATION")
                return context.resources.getDrawable(getStyle(type).iconId)
            }

            private fun getStyle(type: LogEntryType) = STYLE_LOOKUP[type.ordinal]
        }
    }
}

private data class LogEntryStyle(val iconId: Int, val colorId: Int)