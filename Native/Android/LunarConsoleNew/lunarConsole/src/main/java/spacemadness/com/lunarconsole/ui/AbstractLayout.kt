package spacemadness.com.lunarconsole.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import spacemadness.com.lunarconsole.core.Disposable
import spacemadness.com.lunarconsole.reactive.CompositeDisposable

abstract class AbstractLayout(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Disposable {
    private val disposables = CompositeDisposable()

    //region Helpers

    protected fun register(vararg subscriptions: Disposable) {
        disposables.add(*subscriptions)
    }

    //endregion

    //region Disposable

    override fun dispose() {
        disposables.dispose()
    }

    //endregion
}