package spacemadness.com.lunarconsole.ui

import android.graphics.drawable.Drawable
import android.widget.TextView

fun TextView.setDrawables(
    left: Drawable? = null,
    top: Drawable? = null,
    right: Drawable? = null,
    bottom: Drawable? = null
) = setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)