package spacemadness.com.lunarconsole.test

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import spacemadness.com.lunarconsole.test.rules.DependencyProviderRule

abstract class TestCase(enableConsoleOutput: Boolean = false) {
    @get:Rule
    val dependencyRule = DependencyProviderRule(enableConsoleOutput)

    private val results = mutableListOf<Any>()

    //region Before/After

    @Before
    fun setUp() {
        results.clear()
    }

    //endregion

    //region Results

    protected fun addResult(result: Any) {
        results.add(result)
    }

    protected fun assertResults(vararg expected: Any, clearResults: Boolean = true) {
        Assert.assertEquals(expected.toList(), results)
        if (clearResults) {
            results.clear()
        }
    }

    //endregion
}