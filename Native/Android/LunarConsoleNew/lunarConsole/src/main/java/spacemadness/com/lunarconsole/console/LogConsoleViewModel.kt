package spacemadness.com.lunarconsole.console

class LogConsoleViewModel(private val console: LogConsole) {
    val dataSource = object : DataSource<LogEntry> {
        override fun getItemCount() = console.itemCount
        override fun getItem(position: Int) = console.get(position)
    }
}