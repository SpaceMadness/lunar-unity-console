package spacemadness.com.lunarconsole.console

interface DataSource<out T : Any> {
    fun getItemCount(): Int
    fun getItem(position: Int): T
}
