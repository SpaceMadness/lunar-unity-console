package spacemadness.com.lunarconsole.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import spacemadness.com.lunarconsole.debug.Assert;

public class UIUtils
{
    public static void showToast(Context context, String message)
    {
        Assert.IsNotNull(context);
        if (context != null)
        {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: return more generic super type (like ViewGroup)
    public static FrameLayout getRootLayout(Activity activity)
    {
        final ViewGroup viewGroup = getRootViewGroup(activity);
        Assert.IsTrue(viewGroup instanceof FrameLayout);

        return ObjectUtils.as(viewGroup, FrameLayout.class);
    }

    public static ViewGroup getRootViewGroup(Activity activity)
    {
        if (activity == null)
        {
            throw new NullPointerException("Activity is null");
        }

        View rootView = activity.getWindow().findViewById(android.R.id.content);
        Assert.IsTrue(rootView instanceof ViewGroup);

        return ObjectUtils.as(rootView, ViewGroup.class);
    }

    public static float dpToPx(Context context, float dp)
    {
        return dp * getScreenDensity(context);
    }

    public static float pxToDp(Context context, float px)
    {
        return px / getScreenDensity(context);
    }

    private static float getScreenDensity(Context context)
    {
        return context.getResources().getDisplayMetrics().density;
    }
}
