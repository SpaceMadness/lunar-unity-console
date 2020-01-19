package spacemadness.com.lunarconsole.di

import org.junit.Assert.*
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class DependencyProviderTest : TestCase() {
    private val dependency1 = Dependency1()
    private val dependency2 = Dependency2()

    @Test
    fun testRegisterDependencies() {
        DependencyProvider.register(dependency1)
        DependencyProvider.register(dependency2)

        assertSame(dependency1, DependencyProvider.of<Dependency1>())
        assertSame(dependency2, DependencyProvider.of<Dependency2>())
    }

    @Test
    fun testRegisterProviders() {
        DependencyProvider.register(Provider1())
        DependencyProvider.register(Provider2())

        assertSame(dependency1, DependencyProvider.of<Dependency1>())
        assertSame(dependency2, DependencyProvider.of<Dependency2>())
    }

    private class Dependency1
    private class Dependency2

    private inner class Provider1 : Provider<Dependency1> {
        override fun get(): Dependency1 = dependency1
    }

    private inner class Provider2 : Provider<Dependency2> {
        override fun get(): Dependency2 = dependency2
    }
}