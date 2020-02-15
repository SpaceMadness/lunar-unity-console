package spacemadness.com.lunarconsole.ui

import android.app.AlertDialog
import android.content.Context

fun showDialog(
    context: Context,
    title: String,
    message: String? = null,
    positiveButton: DialogButton? = null,
    negativeButton: DialogButton? = null
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    positiveButton?.let { button ->
        builder.setPositiveButton(button.title) { _, _ ->
            button.handler()
        }
    }
    negativeButton?.let { button ->
        builder.setNegativeButton(button.title) { _, _ ->
            button.handler()
        }
    }

    builder.create().show()
}

data class DialogButton(val title: String, val handler: () -> Unit = {})