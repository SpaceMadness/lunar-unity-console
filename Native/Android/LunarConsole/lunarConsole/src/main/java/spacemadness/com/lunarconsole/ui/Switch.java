package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

/**
 * Created by alementuev on 1/23/17.
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Switch extends android.widget.Switch
{
    public Switch(Context context)
    {
        super(context);
    }

    public Switch(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Switch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
