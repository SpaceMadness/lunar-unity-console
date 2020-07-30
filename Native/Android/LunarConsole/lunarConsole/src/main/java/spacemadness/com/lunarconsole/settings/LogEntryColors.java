//
//  LogEntryColors.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
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


package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.json.Required;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class LogEntryColors {
    public @Required
    Color foreground;
    public @Required
    Color background;

    //region Equality

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntryColors that = (LogEntryColors) o;

        if (foreground != null ? !foreground.equals(that.foreground) : that.foreground != null)
            return false;
        return background != null ? background.equals(that.background) : that.background == null;
    }

    @Override
    public int hashCode() {
        int result = foreground != null ? foreground.hashCode() : 0;
        result = 31 * result + (background != null ? background.hashCode() : 0);
        return result;
    }

    //endregion

    @Override
    public String toString() {
        return StringUtils.format("foreground=%s background=%s", foreground, background);
    }
}
