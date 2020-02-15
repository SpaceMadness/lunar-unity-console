package spacemadness.com.lunarconsole.console

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lunar_console_layout_console_log_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.reactive.Observer
import spacemadness.com.lunarconsole.ui.AbstractLayout
import spacemadness.com.lunarconsole.ui.Router

class LogConsoleView(
    context: Context,
    viewModel: LogConsoleViewModel,
    router: Router
) : AbstractLayout(context) {
    private val recyclerView: RecyclerView
    private var scrollLocked = true

    init {
        inflate(context, R.layout.lunar_console_layout_console_log_view, this)

        // setup recycler view
        val adapter = LogEntryListAdapter(viewModel.dataSource)
        adapter.onClickListener = { entry, position ->
            viewModel.onEntryClick(entry, position)
        }
        adapter.onLongClickListener = { entry, position ->
            viewModel.onEntryLongClick(entry, position)
        }

        recyclerView = lunar_console_log_view_recycler_view
        recyclerView.itemAnimator = null // disable animation
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // subscribe to diff stream
        subscribe(viewModel.diffStream, createDiffObserver(adapter))

        // subscribe to overflow stream
        subscribe(viewModel.overflowStream) {
            if (it != null) {
                lunar_console_text_overflow.visibility = View.VISIBLE
                lunar_console_text_overflow.text = resources.getString(
                    R.string.lunar_console_overflow_warning_text, it
                )
            } else {
                lunar_console_text_overflow.visibility = View.GONE
            }
        }

        // subscribe to selected entry stream
        subscribe(viewModel.selectedEntryStream) { entry ->
            router.showLogDetails(context, entry)
        }

        // setup counter buttons
        subscribe(viewModel.logCounterStream) { lunar_console_log_button.text = it }
        subscribe(viewModel.warnCounterStream) { lunar_console_warning_button.text = it }
        subscribe(viewModel.errorCounterStream) { lunar_console_error_button.text = it }

        // clear button
        lunar_console_button_clear.setOnClickListener {
            viewModel.clearLogs()
        }

        // lock button
        subscribe(viewModel.toggleLockStream) { locked ->
            lunar_console_button_lock.isSelected = locked
            scrollLocked = locked
            scrollToBottom()
        }
        
        lunar_console_button_lock.setOnClickListener {
            viewModel.toggleLock()
        }

        // copy button
        lunar_console_button_copy.setOnClickListener {
            viewModel.copyLogs()
        }

        // email button
        lunar_console_button_email.setOnClickListener {
            viewModel.emailLogs()
        }
    }

    //region Observers

    private fun createDiffObserver(adapter: LogEntryListAdapter): Observer<LogEntryList.Diff> {
        return { diff ->
            if (diff.trimCount > 0) {
                adapter.notifyItemRangeRemoved(0, diff.trimCount)
            }
            if (diff.addCount > 0) {
                adapter.notifyItemRangeInserted(diff.totalCount - diff.addCount, diff.addCount)
            }
            if (diff.dirtyCollapsedEntries.size > 0) {
                var i = 0
                while (i < diff.dirtyCollapsedEntries.size) {
                    adapter.notifyItemChanged(diff.dirtyCollapsedEntries[i].position)
                    ++i
                }
            }
//            if (diff.totalTrimCount != prevTotalTrimCount) {
//                if (diff.totalTrimCount > 0) {
//                    lunar_console_text_overflow.visibility = View.VISIBLE
//                    lunar_console_text_overflow.text = resources.getString(
//                        R.string.lunar_console_overflow_warning_text,
//                        toCounterString(diff.totalTrimCount)
//                    )
//                } else {
//                    lunar_console_text_overflow.visibility = View.INVISIBLE
//                }
//                prevTotalTrimCount = diff.totalTrimCount
//            }

            scrollToBottom()
        }
    }

    //endregion

    //region Scroll

    private fun scrollToBottom() {
        if (scrollLocked) {
            recyclerView.scrollToBottom()
        }
    }

    //endregion
}

private fun RecyclerView.scrollToBottom() {
    val itemCount = adapter?.itemCount ?: 0
    if (itemCount > 0) {
        scrollToPosition(itemCount - 1)
    }
}