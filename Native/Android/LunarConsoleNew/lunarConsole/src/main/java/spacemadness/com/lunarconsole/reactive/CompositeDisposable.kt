package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

class CompositeDisposable : Disposable {
    private val children = mutableListOf<Disposable>()

    fun add(disposable: Disposable): CompositeDisposable {
        children.add(disposable)
        return this
    }

    fun add(vararg disposables: Disposable): CompositeDisposable {
        children.addAll(disposables)
        return this
    }

    override fun dispose() {
        children.forEach { it.dispose() }
        children.clear()
    }
}