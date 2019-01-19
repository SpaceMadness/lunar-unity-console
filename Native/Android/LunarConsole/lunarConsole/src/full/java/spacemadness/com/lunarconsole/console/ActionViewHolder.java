//
//  ActionViewHolder.java
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

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;

public class ActionViewHolder extends ConsoleActionAdapter.ViewHolder<Action>
{
    private final View layout;
    private final TextView nameView;

    public ActionViewHolder(View itemView)
    {
        super(itemView);

        layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
        nameView = (TextView) itemView.findViewById(R.id.lunar_console_action_entry_name);
    }

    @Override
    public void onBindViewHolder(Action action, int position)
    {
        Context context = getContext();
        layout.setBackgroundColor(action.getBackgroundColor(context, position));
        nameView.setText(action.name());
    }
}
