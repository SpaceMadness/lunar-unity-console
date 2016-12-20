package spacemadness.com.lunarconsole.console;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.core.KeyboardManager;
import spacemadness.com.lunarconsole.debug.Assert;
import spacemadness.com.lunarconsole.ui.MoveResizeView;
import spacemadness.com.lunarconsole.ui.ViewPager;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ConsoleView extends LinearLayout implements Destroyable
{
    private final WeakReference<Activity> activityRef;
    private final ConsoleLogView consoleLogView;
    private final ConsoleActionView consoleActionView;
    private final KeyboardManager keyboardManager;

    /** An overlay layout for move/resize operations */
    private MoveResizeView moveResizeView;

    private Listener listener;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    public ConsoleView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity);

        if (consolePlugin == null)
        {
            throw new NullPointerException("Console plugin is null");
        }

        activityRef = new WeakReference<>(activity);

        keyboardManager = new KeyboardManager();

        View rootView = LayoutInflater.from(activity).inflate(R.layout.lunar_console_layout_console_view, this, false);
        addView(rootView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.lunar_console_view_pager);

        consoleLogView = new ConsoleLogView(activity, consolePlugin.getConsole());
        viewPager.addPageView(consoleLogView);

        consoleActionView = new ConsoleActionView(activity, consolePlugin.getActionRegistry());
        viewPager.addPageView(consoleActionView);

        consoleLogView.setOnMoveSizeListener(new ConsoleLogView.OnMoveSizeListener()
        {
            @Override
            public void onMoveResize(ConsoleLogView consoleLogView)
            {
                showMoveResizeView(getContext());
            }
        });

        // focus
        setFocusable(true);
        setFocusableInTouchMode(true);

        // setup all edit text view to properly handle virtual keyboard
        setupVirtualKeyboardOnEditTextViews(consoleLogView);
        setupVirtualKeyboardOnEditTextViews(consoleActionView);

        // setup close button
        ImageButton closeButton = (ImageButton) rootView.findViewById(R.id.lunar_console_button_close);
        closeButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyClose();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Move/Resize

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showMoveResizeView(Context context)
    {
        Assert.IsNull(moveResizeView);
        if (moveResizeView == null)
        {
            final FrameLayout parentLayout = ObjectUtils.as(getParent(), FrameLayout.class);
            Assert.IsNotNull(parentLayout);

            if (parentLayout != null)
            {
                moveResizeView = new MoveResizeView(context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
                parentLayout.addView(moveResizeView, layoutParams);

                final MarginLayoutParams p = (MarginLayoutParams) getLayoutParams();
                moveResizeView.setMargins(p.leftMargin, p.topMargin, p.rightMargin, p.bottomMargin);

                // hide page views
                setPageViewsVisible(false);

                // handle close button
                moveResizeView.setOnCloseListener(new MoveResizeView.OnCloseListener()
                {
                    @Override
                    public void onClose(MoveResizeView view)
                    {
                        hideMoveResizeView();
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideMoveResizeView()
    {
        Assert.IsNotNull(moveResizeView);
        if (moveResizeView != null)
        {
            final MarginLayoutParams parentLayoutParams = (MarginLayoutParams) getLayoutParams();

            parentLayoutParams.topMargin = moveResizeView.getTopMargin();
            parentLayoutParams.bottomMargin = moveResizeView.getBottomMargin();
            parentLayoutParams.leftMargin = moveResizeView.getLeftMargin();
            parentLayoutParams.rightMargin = moveResizeView.getRightMargin();
            invalidate();

            // update state margins
            ConsoleViewState.instance().setMargins(moveResizeView.getTopMargin(),
                    moveResizeView.getBottomMargin(),
                    moveResizeView.getLeftMargin(),
                    moveResizeView.getRightMargin());

            final ViewGroup parent = (ViewGroup) moveResizeView.getParent();
            parent.removeView(moveResizeView);

            moveResizeView.destroy();
            moveResizeView = null;

            // show page views
            setPageViewsVisible(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setPageViewsVisible(boolean visible)
    {
        int visibility = visible ? VISIBLE : GONE;

        consoleLogView.setVisibility(visibility);
        consoleActionView.setVisibility(visibility);
    }

    private boolean isMoveResizeViewVisible()
    {
        return moveResizeView != null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Back button

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event)
    {
        /*
        This part is a bit hacky: we want to hide console view on back button press and not finish
        the current activity. We intercept the event and don't let the system to handle it.
        Handling soft keyboard is a separate case: we set a boolean flag when user touches the filter
        input field and hide the keyboard manually if the flag is set. Without this hack the console
        view will be hidden when user dismisses the keyboard. The solution is quite ugly but I don't
        know a better way.
         */
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                handleBackButton();
            }
            return true;
        }

        return super.dispatchKeyEventPreIme(event);
    }

    private void handleBackButton()
    {
        // TODO: maintain a proper stack
        if (keyboardManager.hideSoftKeyboard(getActivity()))
        {
            // nothing to do here
        }
        else if (isMoveResizeViewVisible())
        {
            hideMoveResizeView();
        }
        else
        {
            notifyClose();
        }
    }

    private void setupVirtualKeyboardOnEditTextViews(View rootView)
    {
        if (rootView instanceof ViewGroup)
        {
            ViewGroup group = (ViewGroup) rootView;
            for (int childIndex = 0; childIndex < group.getChildCount(); ++childIndex)
            {
                setupVirtualKeyboardOnEditTextViews(group.getChildAt(childIndex));
            }
        }
        else if (rootView instanceof EditText)
        {
            EditText editText = (EditText) rootView;
            keyboardManager.setupEditText(editText);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Destroyable

    @Override
    public void destroy()
    {
        consoleLogView.destroy();
        consoleActionView.destroy();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener notifications

    void notifyOpen()
    {
        if (listener != null)
        {
            listener.onOpen(this);
        }
    }

    void notifyClose()
    {
        if (listener != null)
        {
            listener.onClose(this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters/Setters

    public Listener getListener()
    {
        return listener;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public Activity getActivity()
    {
        return activityRef.get();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener

    public interface Listener
    {
        void onOpen(ConsoleView view);
        void onClose(ConsoleView view);
    }
}
