package spacemadness.com.lunarconsole.console

import org.junit.Assert
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class RegistrarTest : TestCase() {
    @Test
    fun testRegisterActions() {
        val registrar = Registrar()
        registrar.actionStream.subscribe { actions -> actions.forEach { addResult("action: id=${it.actionId} name='${it.name}'") } }

        Assert.assertTrue(registrar.registerAction(Action(1, "action-1")))
        assertResults(
                "action: id=1 name='action-1'"
        )

        Assert.assertTrue(registrar.registerAction(Action(2, "action-2")))
        assertResults(
                "action: id=1 name='action-1'",
                "action: id=2 name='action-2'"
        )

        Assert.assertFalse(registrar.unregisterAction(3))
        assertResults()

        Assert.assertFalse(registrar.registerAction(Action(2, "action-2")))
        assertResults()

        Assert.assertTrue(registrar.unregisterAction(2))
        assertResults(
                "action: id=1 name='action-1'"
        )

        Assert.assertFalse(registrar.unregisterAction(2))
        assertResults()

        Assert.assertTrue(registrar.unregisterAction(1))
        assertResults()
    }

    @Test
    fun testRegisterVariables() {
        val registrar = Registrar()
        registrar.variableStream.subscribe { variables -> variables.forEach { addResult("variable: id=${it.actionId} name='${it.name}'") } }

        Assert.assertTrue(registrar.registerVariable(createVariable(1, "variable-1")))
        assertResults(
                "variable: id=1 name='variable-1'"
        )

        Assert.assertTrue(registrar.registerVariable(createVariable(2, "variable-2")))
        assertResults(
                "variable: id=1 name='variable-1'",
                "variable: id=2 name='variable-2'"
        )

        Assert.assertFalse(registrar.unregisterVariable(3))
        assertResults()

        Assert.assertFalse(registrar.registerVariable(createVariable(2, "variable-2")))
        assertResults()

        Assert.assertTrue(registrar.unregisterVariable(2))
        assertResults(
                "variable: id=1 name='variable-1'"
        )

        Assert.assertFalse(registrar.unregisterVariable(2))
        assertResults()

        Assert.assertTrue(registrar.unregisterVariable(1))
        assertResults()
    }

    private fun createVariable(id: Int, name: String) =
            Variable(id, name, "value", "default", VariableType.String)
}