//
//  LUCVar.java
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
import android.widget.EditText;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;
import spacemadness.com.lunarconsole.ui.Switch;
import spacemadness.com.lunarconsole.utils.ObjectUtils;

public class LUCVar extends LUEntry
{
    public final LUCVarType type;
    public final String defaultValue;
    public String value;

    public LUCVar(int entryId, String name, String value, String defaultValue, LUCVarType type)
    {
        super(entryId, name);

        this.value = value;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    @Override
    public LUEntryType getEntryType()
    {
        return LUEntryType.Variable;
    }

    //region Default value

    public void resetToDefaultValue()
    {
        value = defaultValue;
    }

    public boolean isDefaultValue()
    {
        return ObjectUtils.areEqual(value, defaultValue);
    }

    //endregion

    //region ViewHolder

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUCVar>
    {
        private final View layout;
        private final TextView nameView;
        private final EditText valueView;
        private final Switch switchView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
            nameView = (TextView) itemView.findViewById(R.id.lunar_console_variable_name);
            valueView = (EditText) itemView.findViewById(R.id.lunar_console_variable_value);
            switchView = (Switch) itemView.findViewById(R.id.lunar_console_variable_switch);
        }

        @Override
        public void onBindViewHolder(LUCVar cvar, int position)
        {
            Context context = getContext();
            layout.setBackgroundColor(cvar.getBackgroundColor(context, position));
            nameView.setText(cvar.name());

            if (cvar.type == LUCVarType.Boolean)
            {
                valueView.setVisibility(View.GONE);
                switchView.setVisibility(View.VISIBLE);
            }
            else
            {
                valueView.setVisibility(View.VISIBLE);
                switchView.setVisibility(View.GONE);
                valueView.setText(cvar.value);
            }
        }
    }

    //endregion
}
