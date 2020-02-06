package spacemadness.com.lunarconsole.console

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.lunar_console_layout_console_log_view.view.*
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.core.CompositeDisposable
import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.core.Observer

class LogConsoleView(context: Context, viewModel: LogConsoleViewModel) : LinearLayout(context), Disposable {
    private val disposables = CompositeDisposable()

    init {
        inflate(context, R.layout.lunar_console_layout_console_log_view, this)

        // setup recycler view
        val adapter = LogEntryListAdapter(viewModel.dataSource)
        val recyclerView = lunar_console_log_view_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // subscribe to diff stream
        disposables.add(
            viewModel.diffStream.subscribe(createDiffObserver(adapter))
        )

        // setup counter
        disposables.add(
            viewModel.counterStream.subscribe(createCounterObserver())
        )
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
        }
    }

    private fun createCounterObserver(): Observer<LogCounter> {
        var prevLog = -1
        var prevWarn = -1
        var prevError = -1
        return { counter ->
            if (prevLog != counter.log) {
                lunar_console_log_button.text = toCounterString(counter.log)
                prevLog = counter.log
            }
            if (prevWarn != counter.warn) {
                lunar_console_warning_button.text = toCounterString(counter.warn)
                prevWarn = counter.warn
            }
            if (prevError != counter.error) {
                lunar_console_error_button.text = toCounterString(counter.error)
                prevError = counter.error
            }
        }
    }

    //endregion

    //region Disposable

    override fun dispose() {
        disposables.dispose()
    }

    //endregion

    companion object {
        private const val MAX_COUNTER = 999

        private fun toCounterString(count: Int): String {
            if (count < MAX_COUNTER) {
                return count.toString()
            }

            return "$count+"
        }
    }
}