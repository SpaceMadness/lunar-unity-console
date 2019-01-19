//
//  StackTrace.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2019 Alex Lementuev, SpaceMadness.
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

import spacemadness.com.lunarconsole.debug.Log;

public class StackTrace
{
    public static final String MARKER_AT = " (at ";
    public static final String MARKER_ASSETS = "/Assets/";

    public static String optimize(String stackTrace)
    {
        try
        {
            if (stackTrace != null && stackTrace.length() > 0)
            {
                String[] lines = stackTrace.split("\n");
                String[] newLines = new String[lines.length];
                for (int i = 0; i < lines.length; ++i)
                {
                    newLines[i] = optimizeLine(lines[i]);
                }

                return StringUtils.Join(newLines, "\n");
            }
        }
        catch (Exception e)
        {
            Log.e(e, "Error while optimizing stacktrace: %s", stackTrace);
        }

        return stackTrace;
    }

    private static String optimizeLine(String line)
    {
        int start = line.indexOf(MARKER_AT);
        if (start == -1) return line;

        int end = line.lastIndexOf(MARKER_ASSETS);
        if (end == -1) return line;

        return line.substring(0, start + MARKER_AT.length()) + line.substring(end + 1);
    }
}
