package spacemadness.com.lunarconsole.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ViewPager extends HorizontalScrollView
{
    private LinearLayout contentLayout;

    public ViewPager(Context context)
    {
        super(context);
        setupUI(context);
    }

    public ViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupUI(context);
    }

    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setupUI(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupUI(context);
    }

    private void setupUI(Context context)
    {
        contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(contentLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int size = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < contentLayout.getChildCount(); i++)
        {
            View child = contentLayout.getChildAt(i);
            if (child.getLayoutParams().width != size)
            {
                child.setLayoutParams(new LinearLayout.LayoutParams(size, LayoutParams.MATCH_PARENT));
            }

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected float getLeftFadingEdgeStrength()
    {
        return 0.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength()
    {
        return 0.0f;
    }

    public void addPageView(View child)
    {
        int width = getWidth();
        child.setLayoutParams(new LayoutParams(width, LayoutParams.MATCH_PARENT));
        contentLayout.addView(child);
        contentLayout.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);

        int width = getWidth();

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            int pageIndex = (getScrollX() + width / 2) / width;
            smoothScrollTo(pageIndex * width, 0);
        }

        return result;
    }
}