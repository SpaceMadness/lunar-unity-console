package spacemadness.com.lunarconsole.utils;

import android.app.Activity;
import android.os.Build;
import android.view.DisplayCutout;

public final class DisplayCutoutHelper {
    private static Margins cachedMargins;

    private DisplayCutoutHelper() {
    }

    public static Margins getSafeMargins(Activity activity) {
        if (cachedMargins != null) {
            return cachedMargins;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DisplayCutout displayCutout = activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                if (displayCutout != null) {
                    return cachedMargins = new Margins(
                            displayCutout.getSafeInsetTop(),
                            displayCutout.getSafeInsetBottom(),
                            displayCutout.getSafeInsetLeft(),
                            displayCutout.getSafeInsetRight()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cachedMargins = new Margins(0, 0, 0, 0);
    }
}
