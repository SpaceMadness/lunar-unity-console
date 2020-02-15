package spacemadness.com.lunarconsole.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.reactive.CompositeDisposable
import spacemadness.com.lunarconsole.reactive.Observable
import spacemadness.com.lunarconsole.reactive.Observer

abstract class AbstractLayout(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Disposable {
    private val disposables = CompositeDisposable()

    //region Helpers

    protected fun <T> subscribe(observable: Observable<T>, observer: Observer<T>): Disposable {
        val subscription = observable.subscribe(observer)
        disposables.add(subscription)
        return subscription
    }

    protected fun dispose(subscription: Disposable) {
        disposables.remove(subscription)
        subscription.dispose()
    }

    //endregion

    //region Disposable

    override fun dispose() {
        disposables.dispose()
    }

    //endregion
}