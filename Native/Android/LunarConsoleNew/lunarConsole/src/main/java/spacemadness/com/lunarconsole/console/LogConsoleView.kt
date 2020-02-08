package spacemadness.com.lunarconsole.console

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lunar_console_layout_console_log_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.reactive.CompositeDisposable
import spacemadness.com.lunarconsole.reactive.Observer

class LogConsoleView(
    context: Context,
    viewModel: LogConsoleViewModel
) : LinearLayout(context), Disposable {
    private val disposables = CompositeDisposable()

    init {
        inflate(context, R.layout.lunar_console_layout_console_log_view, this)

        // setup recycler view
        val adapter = LogEntryListAdapter(viewModel.dataSource)
        val recyclerView = lunar_console_log_view_recycler_view
        recyclerView.itemAnimator = null // disable animation
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // subscribe to diff stream
        disposables.add(
            viewModel.diffStream.subscribe(createDiffObserver(adapter))
        )

        // setup counter buttons
        disposables.add(
            viewModel.logCounterStream.subscribe { lunar_console_log_button.text = it },
            viewModel.warnCounterStream.subscribe { lunar_console_warning_button.text = it },
            viewModel.errorCounterStream.subscribe { lunar_console_error_button.text = it }
        )

        // clear button
        lunar_console_button_clear.setOnClickListener {
            viewModel.clearLogs()
        }

        // lock button
        disposables.add(
            viewModel.toggleLockStream.subscribe { locked ->
                lunar_console_button_lock.isSelected = locked
            }
        )
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
        }
    }

    //endregion

    //region Disposable

    override fun dispose() {
        disposables.dispose()
    }

    //endregion\
}