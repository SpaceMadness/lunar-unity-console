//
//  ConsoleActionView.java
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

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Destroyable;
import spacemadness.com.lunarconsole.utils.UIUtils;

class ConsoleActionView extends AbstractConsoleView implements Destroyable
{
    public ConsoleActionView(Activity activity, ConsolePlugin consolePlugin)
    {
        super(activity, R.layout.lunar_console_layout_console_action_view);

        View contentView = findViewById(R.id.lunar_console_actions_view);
        View warningView = findViewById(R.id.lunar_console_actions_warning_view);

        warningView.setVisibility(VISIBLE);
        contentView.setVisibility(GONE);

        Button getProButton = (Button) findViewById(R.id.lunar_console_no_actions_button_help);
        getProButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = getContext();
                UIUtils.openURL(context, context.getString(R.string.lunar_console_url_actions_get_pro_version));
            }
        });
    }

    @Override
    public void destroy()
    {
    }
}
