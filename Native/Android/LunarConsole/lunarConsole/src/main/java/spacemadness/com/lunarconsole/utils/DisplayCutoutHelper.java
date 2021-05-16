//
//  DisplayCutoutHelper.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
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
