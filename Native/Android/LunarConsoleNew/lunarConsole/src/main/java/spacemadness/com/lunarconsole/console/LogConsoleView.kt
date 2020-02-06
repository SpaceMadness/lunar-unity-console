package spacemadness.com.lunarconsole.console

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.core.CompositeDisposable
import spacemadness.com.lunarconsole.core.Disposable

class LogConsoleView(
    context: Context,
    viewModel: LogConsoleViewModel
) : LinearLayout(context), Disposable {
    private val disposables = CompositeDisposable()

    init {
        inflate(context, R.layout.lunar_console_layout_console_log_view, this)

        val adapter = LogEntryListAdapter(viewModel.dataSource)

        // subscribe to diff stream
        val subscription = viewModel.diffStream.subscribe { diff ->
            if (diff.trimCount > 0) {
                adapter.notifyItemRangeRemoved(0, diff.trimCount)
            }
            if (diff.addCount > 0) {
                adapter.notifyItemRangeInserted(diff.totalCount - diff.addCount, diff.addCount)
            }
            if (diff.dirtyCollapsedEntries.size > 0) {
                var i = 0
                while (i < diff.dirtyCollapsedEntries.size) {
                    adapter.notifyItemChanged(diff.dirtyCollapsedEntries[i].index)
                    ++i
                }
            }
        }
        disposables.add(subscription)

        val recyclerView = findViewById<RecyclerView>(R.id.lunar_console_log_view_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun dispose() {
        disposables.dispose()
    }
}