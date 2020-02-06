package spacemadness.com.lunarconsole.core

class CompositeDisposable : Disposable {
    private val disposables = mutableListOf<Disposable>()

    fun add(disposable: Disposable): CompositeDisposable {
        disposables.add(disposable)
        return this
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
    }
}

fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}