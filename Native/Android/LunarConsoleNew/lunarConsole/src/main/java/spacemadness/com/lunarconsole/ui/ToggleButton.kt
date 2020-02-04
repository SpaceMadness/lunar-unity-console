package spacemadness.com.lunarconsole.ui

import android.content.Context
import android.widget.Button

class ToggleButton(context: Context) : Button(context) {
    var onStateChangeListener: OnStateChangeListener? = null
    private var on = false

    fun init() {
        setOnClickListener { setOn(!on) }
    }

    //region Listener notifications

    private fun notifyStateChanged() {
        onStateChangeListener?.onStateChanged(this@ToggleButton)
    }

    //endregion

    //region Getters/Setters

    // TODO: remove this function
    fun isOn(): Boolean {
        return on
    }

    // TODO: remove this function

    fun setOn(flag: Boolean) {
        if (on != flag) {
            on = flag
            notifyStateChanged()
        }
    }

    //endregion

    //region Listener

    interface OnStateChangeListener {
        fun onStateChanged(button: ToggleButton?)
    }

    //endregion
}