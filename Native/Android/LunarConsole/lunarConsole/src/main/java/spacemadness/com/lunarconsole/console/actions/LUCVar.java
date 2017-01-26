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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
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

    public boolean isDefaultValue()
    {
        return ObjectUtils.areEqual(value, defaultValue);
    }

    //endregion

    //region Getters/Setters

    private boolean boolValue()
    {
        return value != null && value.length() > 0 && !value.equals("0");
    }

    //endregion

    //region ViewHolder

    public static class ViewHolder extends ConsoleActionAdapter.ViewHolder<LUCVar> implements
            CompoundButton.OnCheckedChangeListener, View.OnClickListener
    {
        private final View layout;
        private final TextView nameTextView;
        private final Button valueEditButton;
        private final Switch toggleSwitch;
        private boolean ignoreListenerCallbacks;
        private LUCVar variable; // TODO: weak reference?

        public ViewHolder(View itemView)
        {
            super(itemView);

            layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
            nameTextView = (TextView) itemView.findViewById(R.id.lunar_console_variable_entry_name);
            valueEditButton = (Button) itemView.findViewById(R.id.lunar_console_variable_entry_value);
            toggleSwitch = (Switch) itemView.findViewById(R.id.lunar_console_variable_entry_switch);
            valueEditButton.setOnClickListener(this);
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
                    valueEditButton.setVisibility(View.GONE);
                    toggleSwitch.setVisibility(View.VISIBLE);
                    toggleSwitch.setChecked(variable.boolValue());
                }
                else
                {
                    valueEditButton.setVisibility(View.VISIBLE);
                    toggleSwitch.setVisibility(View.GONE);
                    valueEditButton.setText(cvar.value);
                }
            }
            finally
            {
                ignoreListenerCallbacks = false;
            }
            updateUI();
        }

        //region OnCheckedChangeListener

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (ignoreListenerCallbacks)
            {
                return;
            }

            updateValue(isChecked ? "1" : "0");
        }

        //endregion

        //region View.OnClickListener

        @Override
        public void onClick(View v)
        {
            final Context context = v.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            final EditText valueEditText = new EditText(context);
            valueEditText.setText(variable.value);
            valueEditText.setSelectAllOnFocus(true);
            builder.setView(valueEditText);

            builder.setMessage(variable.name())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            final String value = valueEditText.getText().toString();

                            switch (variable.type)
                            {
                                case Integer:
                                {
                                    if (!StringUtils.isValidInteger(value))
                                    {
                                        UIUtils.showDialog(layout.getContext(),
                                                context.getString(R.string.lunar_console_variable_value_error_title),
                                                context.getString(R.string.lunar_console_variable_value_error_message_type_integer));
                                        return;
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
                                        return;
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
                                    return;
                                }
                            }

                            updateValue(value);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                        }
                    })
                    .setNeutralButton("Default", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            updateValue(variable.defaultValue);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //endregion

        //region Helpers

        void updateValue(String value)
        {
            variable.value = value;
            NotificationCenter.defaultCenter().postNotification(VARIABLE_SET, VARIABLE_SET_KEY_VARIABLE, variable);
            updateUI();
        }

        void updateUI()
        {
            final int style = variable.isDefaultValue() ? Typeface.NORMAL : Typeface.BOLD;
            nameTextView.setTypeface(null, style);
            if (variable.type == LUCVarType.Boolean)
            {
                toggleSwitch.setTypeface(null, style);
            }
            else
            {
                valueEditButton.setTypeface(null, style);
            }
        }

        //endregion
    }

    //endregion
}
