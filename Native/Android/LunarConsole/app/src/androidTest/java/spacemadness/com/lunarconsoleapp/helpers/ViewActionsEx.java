package spacemadness.com.lunarconsoleapp.helpers;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;

public class ViewActionsEx
{
    public static ViewAction setSeekBarProgress(final int progress)
    {
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()
            {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription()
            {
                return "seek bar progress is expected to be " + progress;
            }

            @Override
            public void perform(UiController uiController, View view)
            {
                ((SeekBar) view).setProgress(progress);
            }
        };
    }
}
