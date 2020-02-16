package spacemadness.com.lunarconsole.ui

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.annotation.MainThread

@MainThread
fun hideSoftInput(context: Context, windowToken: IBinder) {
    val manager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(windowToken, 0)
}