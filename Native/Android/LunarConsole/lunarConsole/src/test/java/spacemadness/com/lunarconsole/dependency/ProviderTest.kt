package spacemadness.com.lunarconsole.dependency

import junit.framework.TestCase

class ProviderTest : TestCase() {
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        Provider.clear()
    }

    fun testProvider() {
        val providable = MyProviderImpl()

        Provider.register(MyProvider::class.java, providable)
        TestCase.assertSame(providable, Provider.of(MyProvider::class.java))
    }

    fun testMissingProvider() {
        try {
            Provider.of(MyProvider::class.java)
            TestCase.fail("Should throw MissingProvidableException")
        } catch (ignored: IllegalArgumentException) {
        }

    }

    private interface MyProvider : ProviderDependency

    private class MyProviderImpl : MyProvider
}