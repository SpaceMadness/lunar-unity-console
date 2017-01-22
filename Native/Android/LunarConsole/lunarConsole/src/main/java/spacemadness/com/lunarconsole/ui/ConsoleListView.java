package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class ConsoleListView extends ListView
{
    public ConsoleListView(Context context)
    {
        super(context);
        setupUI(context);
    }

    public ConsoleListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupUI(context);
    }

    public ConsoleListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setupUI(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConsoleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupUI(context);
    }

    private void setupUI(Context context)
    {
        setDivider(null);
        setDividerHeight(0);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        setScrollingCacheEnabled(false);
    }
}
