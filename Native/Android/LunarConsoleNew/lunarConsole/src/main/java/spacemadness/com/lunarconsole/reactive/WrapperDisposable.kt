package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

internal class WrapperDisposable(private val target: Disposable) :
    Disposable {
    override fun dispose() {
        target.dispose()
    }
}