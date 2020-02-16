package spacemadness.com.lunarconsole.extensions

import android.view.View
import androidx.annotation.MainThread
import spacemadness.com.lunarconsole.ui.hideSoftInput

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

@MainThread
fun View.clearFocusAndHideKeyboard() {
    if (isFocused) {
        clearFocus()
        hideSoftInput(context, windowToken)
    }
}