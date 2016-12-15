package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import spacemadness.com.lunarconsole.core.Destroyable;

public abstract class AbstractConsoleView extends LinearLayout implements Destroyable
{
    private boolean softKeyboardVisible;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public AbstractConsoleView(Context context)
    {
        super(context);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Edit text setup

    protected void setupEditText(EditText editText)
    {
        editText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                softKeyboardVisible = true;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Back button

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
        /*
        This part is a bit hacky: we want to hide console view on back button press and not finish
        the current activity. We intercept the event and don't let the system to handle it.
        Handling soft keyboard is a separate case: we set a boolean flag when user touches the filter
        input field and hide the keyboard manually if the flag is set. Without this hack the console
        view will be hidden when user dismisses the keyboard. The solution is quite ugly but I don't
        know a better way.
         */
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                handleBackButton();
            }
            return true;
        }

        return super.dispatchKeyEventPreIme(event);
    }

    private void handleBackButton()
    {
        if (softKeyboardVisible)
        {
            hideSoftKeyboard();
        }
        else
        {
            onBackButton();
        }
    }

    private void hideSoftKeyboard()
    {
        softKeyboardVisible = false;
        InputMethodManager manager = (InputMethodManager) getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    protected void onBackButton()
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
    }
}
