package spacemadness.com.lunarconsole.console;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

class OverlayDialog extends Dialog
{
    private final int systemUIVisibilityFlags;
    private final Activity activity;
    private BackButtonListener backButtonListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public OverlayDialog(Activity activity, int themeResId)
    {
        super(activity, themeResId);

        this.activity = activity;

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.requestFeature(Window.FEATURE_NO_TITLE);

        // we need to mimic Unity's navigation bar behaviour
        View decorView = activity.getWindow().getDecorView();
        systemUIVisibilityFlags = decorView.getSystemUiVisibility();
        window.getDecorView().setSystemUiVisibility(systemUIVisibilityFlags);
    }

    @Override
    public void onBackPressed()
    {
        if (backButtonListener != null && backButtonListener.onBackPressed())
        {
            // back button was handled - don't dismiss dialog
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return activity.onTouchEvent(event); // pass touch events to the host activity
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            getWindow().getDecorView().setSystemUiVisibility(systemUIVisibilityFlags);
        }
    }

    public void setBackButtonListener(BackButtonListener backButtonListener)
    {
        this.backButtonListener = backButtonListener;
    }
}
