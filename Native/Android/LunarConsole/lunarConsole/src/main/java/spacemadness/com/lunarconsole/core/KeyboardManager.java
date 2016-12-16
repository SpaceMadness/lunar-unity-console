package spacemadness.com.lunarconsole.core;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class KeyboardManager
{
    private boolean softKeyboardVisible;
    private final View rootView;

    public KeyboardManager(View rootView)
    {
        if (rootView == null)
        {
            throw new NullPointerException("Root view is null");
        }

        this.rootView = rootView;
    }

    public void setupEditText(EditText editText)
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
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
                    return hideSoftKeyboard();
                }
                return false;
            }
        });
    }

    public boolean hideSoftKeyboard()
    {
        if (softKeyboardVisible)
        {
            softKeyboardVisible = false;

            InputMethodManager manager = (InputMethodManager) rootView.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

            return true;
        }

        return false;
    }
}
