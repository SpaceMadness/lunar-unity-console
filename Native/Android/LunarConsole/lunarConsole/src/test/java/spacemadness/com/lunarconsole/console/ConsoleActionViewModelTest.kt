package spacemadness.com.lunarconsole.console

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.rx.Observer
import spacemadness.com.lunarconsole.ui.ListViewItem

class ConsoleActionViewModelTest : TestCase() {
    @Test
    fun testListItems() {
        val registrar = Registrar()
        val model = ConsoleActionViewModel(registrar)
        model.itemsStream.subscribe { items -> items.forEach { addResult(it.toString()) } }
    }
}