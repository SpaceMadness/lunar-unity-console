//
//  LogOverlaySettings.java
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

/**
 * Log overlay settings.
 */
public final class LogOverlaySettings {
    /**
     * Indicates if log overlay is enabled.
     */
    public boolean enabled;

    /**
     * Max number of simultaneously visible lines.
     */
    public int maxVisibleLines;

    /**
     * Delay in seconds before each line disappears (<code>0</code> means never disappear)
     */
    public float timeout;

    /**
     * Indicates if the line background should be transparent.
     */
    public @Required
    LogColors colors;

    //region Equality

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogOverlaySettings that = (LogOverlaySettings) o;

        if (enabled != that.enabled) return false;
        if (maxVisibleLines != that.maxVisibleLines) return false;
        if (Float.compare(that.timeout, timeout) != 0) return false;
        return colors != null ? colors.equals(that.colors) : that.colors == null;
    }

    @Override
    public int hashCode() {
        int result = (enabled ? 1 : 0);
        result = 31 * result + maxVisibleLines;
        result = 31 * result + (timeout != +0.0f ? Float.floatToIntBits(timeout) : 0);
        result = 31 * result + (colors != null ? colors.hashCode() : 0);
        return result;
    }

    //endregion
}
