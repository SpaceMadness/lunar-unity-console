package spacemadness.com.lunarconsole.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import spacemadness.com.lunarconsole.debug.Log;
import static spacemadness.com.lunarconsole.utils.ObjectUtils.*;
import static spacemadness.com.lunarconsole.utils.UIUtils.*;

public class KeyboardManager
{
    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;

    public static boolean isKeyboardVisible(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("Activity is null");
        }

        Rect r = new Rect();

        View activityRoot = getActivityRoot(activity);
        int visibleThreshold =
                Math.round(dpToPx(activity, KEYBOARD_VISIBLE_THRESHOLD_DP));

        activityRoot.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRoot.getRootView().getHeight() - r.height();
        return heightDiff > visibleThreshold;
    }

    public static boolean hideSoftKeyboard(Activity activity)
    {
        if (isKeyboardVisible(activity))
        {
            View rootView = getActivityRoot(activity);

            InputMethodManager manager = (InputMethodManager) activity.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

            return true;
        }

        return false;
    }

    public static void setupEditText(EditText editText)
    {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                Activity activity = as(v.getContext(), Activity.class);
                if (activity == null)
                {
                    Log.e("Unable to handle IME_ACTION_SEARCH: context is not activity");
                    return false;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    return hideSoftKeyboard(activity);
                }
                return false;
            }
        });
    }

    private static View getActivityRoot(Activity activity)
    {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }
}
