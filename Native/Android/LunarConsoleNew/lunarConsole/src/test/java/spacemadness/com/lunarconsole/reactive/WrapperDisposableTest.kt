package spacemadness.com.lunarconsole.reactive

import org.junit.Test

import org.junit.Assert.*
import spacemadness.com.lunarconsole.TestCase
import spacemadness.com.lunarconsole.core.Disposable

class WrapperDisposableTest {
    @Test
    fun dispose() {
        var disposed = false
        val disposable = object : Disposable {
            override fun dispose() {
                disposed = true
            }
        }

        WrapperDisposable(disposable).dispose()
        assertTrue(disposed)
    }
}