//
//  VariableType.java
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

import spacemadness.com.lunarconsole.debug.Log;

public enum VariableType
{
    Unknown, Boolean, Integer, Float, String;

    public static VariableType parse(String name)
    {
        try
        {
            return Enum.valueOf(VariableType.class, name);
        }
        catch (Exception e)
        {
            Log.e(e, "Exception while parsing variable type: %s", name);
            return VariableType.Unknown;
        }
    }
}
