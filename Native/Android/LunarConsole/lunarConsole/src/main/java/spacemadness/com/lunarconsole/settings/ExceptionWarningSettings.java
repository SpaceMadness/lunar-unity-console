//
//  ExceptionWarningSettings.java
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
 * Exception warning settings from Unity editor.
 */
public final class ExceptionWarningSettings {
    /**
     * Content display mode.
     */
    public @Required
    DisplayMode displayMode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionWarningSettings that = (ExceptionWarningSettings) o;

        return displayMode == that.displayMode;
    }

    //region Equality

    @Override
    public int hashCode() {
        return displayMode != null ? displayMode.hashCode() : 0;
    }

    public enum DisplayMode {
        /**
         * Don't display anything.
         */
        NONE,

        /**
         * Display only errors.
         */
        ERRORS,

        /**
         * Display only exceptions.
         */
        EXCEPTIONS,

        /**
         * Display everything.
         */
        ALL
    }

    //endregion
}
