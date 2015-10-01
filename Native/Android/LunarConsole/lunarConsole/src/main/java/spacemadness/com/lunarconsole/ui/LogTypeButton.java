package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class LogTypeButton extends ToggleButton
{
    private static final int MAX_COUNT = 999;

    private int count;
    private float offAlpha;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LogTypeButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LogTypeButton(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        count = Integer.MAX_VALUE;
        offAlpha = 0.25f;
    }

    @Override
    public void setOn(boolean flag)
    {
        super.setOn(flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setAlpha(flag ? 1.0f : offAlpha);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public void setCount(int count)
    {
        if (this.count != count)
        {
            if (count < MAX_COUNT)
            {
                setText(Integer.toString(count));
            }
            else if (this.count < MAX_COUNT)
            {
                setText(MAX_COUNT + "+");
            }
            this.count = count;
        }
    }

    public void setOffAlpha(float offAlpha)
    {
        this.offAlpha = offAlpha;
    }

    public float getOffAlpha()
    {
        return offAlpha;
    }
}
