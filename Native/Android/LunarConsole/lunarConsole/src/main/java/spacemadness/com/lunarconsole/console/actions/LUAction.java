//
//  LUAction.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

package spacemadness.com.lunarconsole.console.actions;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;

public class LUAction extends LUEntry
{
    public LUAction(int actionId, String name)
    {
        super(actionId, name);
    }

    @Override
    public LUEntryType getEntryType()
    {
        return LUEntryType.Action;
    }

    //region ViewHolder

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUAction>
    {
        private final View layout;
        private final TextView nameView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
            nameView = (TextView) itemView.findViewById(R.id.lunar_console_action_entry_name);
        }

        @Override
        public void onBindViewHolder(LUAction action, int position)
        {
            Context context = getContext();
            layout.setBackgroundColor(action.getBackgroundColor(context, position));
            nameView.setText(action.name());
        }
    }

    //endregion
}
