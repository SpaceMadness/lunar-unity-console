package spacemadness.com.lunarconsole.app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import spacemadness.com.lunarconsole.console.*
import spacemadness.com.lunarconsole.log.LogLevel
import spacemadness.com.lunarconsole.utils.LimitSizeList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val console = LogConsole(capacity = 4096, trimSize = 512)
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

    val levels = listOf(LogEntryType.LOG, LogEntryType.WARNING, LogEntryType.ERROR)
    var entryIndex = 0
    var levelIndex = 0

    private fun entries(lo: Int, hi: Int): List<LogEntry> {
        // return (lo..hi).map { LogEntry(0, LogEntryType.LOG, "Message-$it", null) }
        val level = levels[levelIndex]
        levelIndex = (levelIndex + 1) % levels.size
        return (lo..hi).map {
            LogEntry(entryIndex++, level, "Message-$it")
        }
    }
}

class Adapter(private val list: LogEntryList) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, null, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(android.R.id.text1)
        val colors = mapOf(
            LogEntryType.LOG to Color.BLACK,
            LogEntryType.WARNING to Color.YELLOW,
            LogEntryType.ERROR to Color.RED
        )

        fun onBind(value: LogEntry) {
            textView.text = value.message
            textView.setTextColor(colors[value.type] ?: Color.MAGENTA)
        }
    }
}
