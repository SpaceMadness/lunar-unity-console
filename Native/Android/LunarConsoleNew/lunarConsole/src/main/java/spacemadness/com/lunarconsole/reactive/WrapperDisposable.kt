package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

/** Disposes target disposable while being disposed */
internal class WrapperDisposable(private val target: Disposable) : Disposable {
    override fun dispose() {
        target.dispose()
    }
}