//
//  UIUtils.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package spacemadness.com.lunarconsole.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import spacemadness.com.lunarconsole.debug.Assert;
import spacemadness.com.lunarconsole.debug.Log;

public class UIUtils
{
    public static boolean openURL(Context context, String url)
    {
        try
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
            return true;
        }
        catch (Exception e)
        {
            Log.e(e, "Exception while opening URL '%s'", url);
            return false;
        }
    }

    public static void showDialog(Context context, String title, String message)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // nothing here
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public static void showToast(Context context, String message)
    {
        Assert.IsNotNull(context);
        if (context != null)
        {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
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
