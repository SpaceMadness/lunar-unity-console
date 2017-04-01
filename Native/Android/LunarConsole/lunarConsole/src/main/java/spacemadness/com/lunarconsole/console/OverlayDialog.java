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
import android.widget.FrameLayout;

import static android.widget.FrameLayout.LayoutParams.*;

class OverlayDialog extends Dialog
{
    private final int systemUIVisibilityFlags;
    private final Activity activity;
    private final FrameLayout contentView;
    private BackButtonListener backButtonListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public OverlayDialog(Activity activity, int themeResId)
    {
        super(activity, themeResId);

        this.activity = activity;

        setCancelable(false);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.requestFeature(Window.FEATURE_NO_TITLE);

        // we need to mimic Unity's navigation bar behaviour
        View decorView = activity.getWindow().getDecorView();
        systemUIVisibilityFlags = decorView.getSystemUiVisibility();
        window.getDecorView().setSystemUiVisibility(systemUIVisibilityFlags);

        // set content view
        contentView = new FrameLayout(activity);
        setContentView(contentView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    public void addView(View view)
    {
        contentView.addView(view);
    }

    public void addView(View view, FrameLayout.LayoutParams params)
    {
        contentView.addView(view, params);
    }

    public void removeView(View view)
    {
        contentView.removeView(view);
    }

    public int getChildCount()
    {
        return contentView.getChildCount();
    }

    @Override
    public void onBackPressed()
    {
        if (backButtonListener != null)
        {
            backButtonListener.onBackPressed();
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

    public void setOnTouchListener(View.OnTouchListener onTouchListener)
    {
        getWindow().getDecorView().setOnTouchListener(onTouchListener);
    }
}
