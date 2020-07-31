package spacemadness.com.lunarconsole.console

import org.junit.Test
import spacemadness.com.lunarconsole.R
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.mock.MockStringProvider
import spacemadness.com.lunarconsole.ui.ListViewItem

class ConsoleActionViewModelTest : TestCase() {
    @Test
    fun testListItems() {
        val registrar = Registrar()
        val model = createViewModel(registrar)

        // no items
        model.itemsStream.subscribe { items ->
            run {
                clearResult()
                items.forEach { addResult(toString(it)) }
            }
        }
        assertResults()

        // populating items
        registrar.registerAction(1, "action-1")
        registrar.registerAction(2, "action-2")
        assertResults(
                "header: title='Actions'",
                "action: id=1 name='action-1'",
                "action: id=2 name='action-2'"
        )

        registrar.unregisterAction(2)
        assertResults(
                "header: title='Actions'",
                "action: id=1 name='action-1'"
        )

        registrar.unregisterAction(1)
        assertResults()

        registrar.registerVariable(3, "variable-1")
        assertResults(
                "header: title='Variables'",
                "variable: id=3 name='variable-1'"
        )

        registrar.registerVariable(4, "variable-2")
        assertResults(
                "header: title='Variables'",
                "variable: id=3 name='variable-1'",
                "variable: id=4 name='variable-2'"
        )

        registrar.unregisterVariable(3)
        assertResults(
                "header: title='Variables'",
                "variable: id=4 name='variable-2'"
        )

        registrar.unregisterVariable(4)
        assertResults()

        registrar.registerAction(5, "action-3")
        registrar.registerVariable(6, "variable-3")

        assertResults(
                "header: title='Actions'",
                "action: id=5 name='action-3'",
                "header: title='Variables'",
                "variable: id=6 name='variable-3'"
        )
    }

    private fun createViewModel(registrar: Registrar): ConsoleActionViewModel {
        return ConsoleActionViewModel(registrar, MockStringProvider().apply {
            add(R.string.header_actions, "Actions")
            add(R.string.header_variables, "Variables")
        })
    }

    private fun toString(it: ListViewItem) = when (it) {
        is HeaderListItem -> "header: title='${it.title}'"
        is ActionListItem -> "action: id=${it.id} name='${it.name}'"
        is VariableListItem -> "variable: id=${it.id} name='${it.name}'"
        else -> throw IllegalArgumentException("Unexpected type: $it")
    }
}

private fun Registrar.registerAction(id: Int, name: String) {
    registerAction(Action(id, name))
}

private fun Registrar.registerVariable(id: Int, name: String) {
    registerVariable(Variable(id, name, "value", "default", VariableType.String))
}