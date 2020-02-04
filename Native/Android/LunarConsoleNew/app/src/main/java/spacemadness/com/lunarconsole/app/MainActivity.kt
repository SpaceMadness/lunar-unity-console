package spacemadness.com.lunarconsole.app

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import spacemadness.com.lunarconsole.console.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val console = LogConsole(capacity = 4096, trimSize = 512)
        console.logMessages(
            listOf(
                LogEntry(0, LogEntryType.LOG, "Log", null),
                LogEntry(1, LogEntryType.WARNING, "Warning", null),
                LogEntry(2, LogEntryType.ERROR, "Error", null)
            )
        )

        val consoleViewModel = LogConsoleViewModel(console)
        val consoleView = LogConsoleView(this, consoleViewModel)
        containerView.addView(consoleView, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

//        val list = LogEntryList(10, 5)
//        list.addAll(entries(1, 5))
//
//        val adapter = Adapter(list)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        var nextEntry = 6
//        button.setOnClickListener {
//            val diff = LogEntryList.Diff()
//            val elements = entries(nextEntry, nextEntry + 2)
//            list.addAll(elements, diff)
//            if (diff.trimCount > 0) {
//                adapter.notifyItemRangeRemoved(0, diff.trimCount)
//            }
//            if (diff.addCount > 0) {
//                adapter.notifyItemRangeInserted(list.count() - diff.addCount, diff.addCount)
//            }
//            nextEntry += 3
//        }
    }
}