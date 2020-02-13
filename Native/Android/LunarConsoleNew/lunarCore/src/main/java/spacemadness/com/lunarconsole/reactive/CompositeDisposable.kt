package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

class CompositeDisposable(list: List<Disposable> = emptyList()) : Disposable {
    private val children = list.toMutableList()

    fun add(disposable: Disposable): CompositeDisposable {
        if (!children.contains(disposable)) {
            children.add(disposable)
        }
        return this
    }

    fun add(vararg disposables: Disposable): CompositeDisposable {
        disposables.forEach { add(it) }
        return this
    }

    override fun dispose() {
        children.forEach { it.dispose() }
        children.clear()
    }
}