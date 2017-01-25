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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.console.ConsoleActionAdapter;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.Switch;
import spacemadness.com.lunarconsole.utils.ObjectUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static spacemadness.com.lunarconsole.console.ConsoleNotifications.*;

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

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUCVar> implements
            CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener
    {
        private final View layout;
        private final TextView nameTextView;
        private final EditText valueEditText;
        private final Switch toggleSwitch;
        private boolean ignoreListenerCallbacks;
        private LUCVar variable; // TODO: weak reference?

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
            nameTextView = (TextView) itemView.findViewById(R.id.lunar_console_variable_name);
            valueEditText = (EditText) itemView.findViewById(R.id.lunar_console_variable_value);
            toggleSwitch = (Switch) itemView.findViewById(R.id.lunar_console_variable_switch);
            valueEditText.setOnEditorActionListener(this);
            toggleSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onBindViewHolder(LUCVar cvar, int position)
        {
            this.variable = cvar;

            Context context = getContext();
            layout.setBackgroundColor(cvar.getBackgroundColor(context, position));

            try
            {
                // we don't want to trigger listener callbacks while setting an initial value
                ignoreListenerCallbacks = true;

                nameTextView.setText(cvar.name());

                if (cvar.type == LUCVarType.Boolean)
                {
                    valueEditText.setVisibility(View.GONE);
                    toggleSwitch.setVisibility(View.VISIBLE);
                }
                else
                {
                    valueEditText.setVisibility(View.VISIBLE);
                    toggleSwitch.setVisibility(View.GONE);
                    valueEditText.setText(cvar.value);
                }
            }
            finally
            {
                ignoreListenerCallbacks = false;
            }
        }

        //region TextView.OnEditorActionListener

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                final String value = v.getText().toString();
                final Context context = v.getContext();

                switch (variable.type)
                {
                    case Integer:
                    {
                        if (!StringUtils.isValidInteger(value))
                        {
                            UIUtils.showDialog(layout.getContext(),
                                    context.getString(R.string.lunar_console_variable_value_error_title),
                                    context.getString(R.string.lunar_console_variable_value_error_message_type_integer));
                            restoreValue(v);
                            return true;
                        }
                        break;
                    }

                    case Float:
                    {
                        if (!StringUtils.isValidFloat(value))
                        {
                            UIUtils.showDialog(layout.getContext(),
                                    context.getString(R.string.lunar_console_variable_value_error_title),
                                    context.getString(R.string.lunar_console_variable_value_error_message_type_float));
                            restoreValue(v);
                            return true;
                        }
                        break;
                    }

                    case String:
                    {
                        // string is always valid
                        break;
                    }

                    default:
                    {
                        Log.e("Unexpected variable type: %s", variable.type);
                        return false;
                    }
                }

                variable.value = value;
                NotificationCenter.defaultCenter().postNotification(VARIABLE_SET, VARIABLE_SET_KEY_VARIABLE, variable);
            }
            return false;
        }

        //endregion

        //region OnCheckedChangeListener

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (ignoreListenerCallbacks)
            {
                return;
            }

            variable.value = isChecked ? "1" : "0";
            NotificationCenter.defaultCenter().postNotification(VARIABLE_SET, VARIABLE_SET_KEY_VARIABLE, variable);
        }

        //endregion

        //region Helpers

        private void restoreValue(TextView v)
        {
            v.setText(variable.value);
            EditText editText = ObjectUtils.as(v, EditText.class);
            if (editText != null)
            {
                editText.setSelection(variable.value.length());
            }
        }

        //endregion
    }

    //endregion
}
