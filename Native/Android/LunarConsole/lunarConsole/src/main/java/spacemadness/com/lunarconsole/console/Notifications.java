//
//  Notifications.java
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

/**
 * Created by alementuev on 12/20/16.
 */

public class Notifications
{
    public static final String NOTIFICATION_ACTIVITY_STARTED = "ACTIVITY_STARTED"; // { activity: Activity }
    public static final String NOTIFICATION_ACTIVITY_STOPPED = "ACTIVITY_STOPPED"; // { activity: Activity }

    public static final String NOTIFICATION_ACTION_SELECT = "ACTION_SELECT";
    public static final String NOTIFICATION_VARIABLE_SET = "VARIABLE_SET";

    public static final String NOTIFICATION_KEY_ACTIVITY = "activity";
    public static final String NOTIFICATION_KEY_ACTION = "action";
    public static final String NOTIFICATION_KEY_VARIABLE = "variable";
}
