//
//  ConsoleLogType.java
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

package spacemadness.com.lunarconsole.console;

public final class ConsoleLogType
{
    public static final byte ERROR = 0;
    public static final byte ASSERT = 1;
    public static final byte WARNING = 2;
    public static final byte LOG = 3;
    public static final byte EXCEPTION = 4;

    public static final byte COUNT = 5;

    public static boolean isErrorType(int type)
    {
        return type == EXCEPTION ||
               type == ERROR ||
               type == ASSERT;
    }

    public static boolean isValidType(int type)
    {
        return type >= 0 && type < COUNT;
    }

    public static int getMask(int type)
    {
        return 1 << type;
    }

    /* The class should not be instantiated */
    private ConsoleLogType()
    {
    }
}
