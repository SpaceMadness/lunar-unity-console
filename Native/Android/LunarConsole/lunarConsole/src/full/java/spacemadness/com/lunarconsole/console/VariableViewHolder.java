//
//  VariableViewHolder.java
//
//  Lunar Network
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

package spacemadness.com.lunarconsole.console;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.Switch;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static spacemadness.com.lunarconsole.console.ConsoleNotifications.VARIABLE_SET;
import static spacemadness.com.lunarconsole.console.ConsoleNotifications.VARIABLE_SET_KEY_VARIABLE;

public class VariableViewHolder extends ConsoleActionAdapter.ViewHolder<Variable> implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener
{
    private final View layout;
    private final TextView nameTextView;
    private final Button valueEditButton;
    private final Switch toggleSwitch;
    private boolean ignoreListenerCallbacks;
    private Variable variable; // TODO: weak reference?

    public VariableViewHolder(View itemView)
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
    public void onBindViewHolder(Variable cvar, int position)
    {
        this.variable = cvar;

        Context context = getContext();
        layout.setBackgroundColor(cvar.getBackgroundColor(context, position));

        try
        {
            // we don't want to trigger listener callbacks while setting an initial value
            ignoreListenerCallbacks = true;

            nameTextView.setText(cvar.name());

            if (cvar.type == VariableType.Boolean)
            {
                valueEditButton.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.VISIBLE);
            } else
            {
                valueEditButton.setVisibility(View.VISIBLE);
                toggleSwitch.setVisibility(View.GONE);
            }

            updateUI();
        }
        finally
        {
            ignoreListenerCallbacks = false;
        }
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
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(variable.name());
        dialog.setContentView(R.layout.lunar_console_layout_edit_variable_dialog);

        final TextView defaultText = (TextView) dialog.findViewById(R.id.lunar_console_edit_variable_default_value);
        defaultText.setText(String.format(getString(R.string.lunar_console_edit_variable_title_default_value), variable.defaultValue));

        final EditText valueEditText = (EditText) dialog.findViewById(R.id.lunar_console_edit_variable_value);
        valueEditText.setText(variable.value);
        valueEditText.setSelectAllOnFocus(true);

        dialog.findViewById(R.id.lunar_console_edit_variable_button_ok).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.lunar_console_edit_variable_button_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.lunar_console_edit_variable_button_reset).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateValue(variable.defaultValue);
                dialog.dismiss();
            }
        });
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void updateUI()
    {
        final int style = variable.isDefaultValue() ? Typeface.NORMAL : Typeface.BOLD;
        nameTextView.setTypeface(null, style);
        if (variable.type == VariableType.Boolean)
        {
            toggleSwitch.setTypeface(null, style);
            toggleSwitch.setChecked(variable.boolValue());
        } else
        {
            valueEditButton.setTypeface(null, style);
            valueEditButton.setText(variable.value);
        }
    }

    //endregion
}
