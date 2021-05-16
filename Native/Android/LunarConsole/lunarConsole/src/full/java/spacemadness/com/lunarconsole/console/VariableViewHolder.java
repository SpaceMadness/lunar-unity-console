//
//  VariableViewHolder.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.NotificationCenter;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.debug.TestHelper;
import spacemadness.com.lunarconsole.ui.Switch;
import spacemadness.com.lunarconsole.utils.CollectionUtils;
import spacemadness.com.lunarconsole.utils.StringUtils;
import spacemadness.com.lunarconsole.utils.UIUtils;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_KEY_VARIABLE;
import static spacemadness.com.lunarconsole.console.Notifications.NOTIFICATION_VARIABLE_SET;

public class VariableViewHolder extends ConsoleActionAdapter.ViewHolder<Variable> implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private final View layout;
    private final TextView nameTextView;
    private final Button valueEditButton;
    private final Switch toggleSwitch;
    private final int originalNameTextColor;
    private boolean ignoreListenerCallbacks;
    private Variable variable; // TODO: weak reference?

    public VariableViewHolder(View itemView) {
        super(itemView);

        layout = itemView.findViewById(R.id.lunar_console_action_entry_layout);
        nameTextView = (TextView) itemView.findViewById(R.id.lunar_console_variable_entry_name);
        originalNameTextColor = nameTextView.getCurrentTextColor();
        valueEditButton = (Button) itemView.findViewById(R.id.lunar_console_variable_entry_value);
        toggleSwitch = (Switch) itemView.findViewById(R.id.lunar_console_variable_entry_switch);
        valueEditButton.setOnClickListener(this);
        toggleSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onBindViewHolder(Variable cvar, int position) {
        this.variable = cvar;

        Context context = getContext();
        layout.setBackgroundColor(cvar.getBackgroundColor(context, position));

        try {
            // we don't want to trigger listener callbacks while setting an initial value
            ignoreListenerCallbacks = true;

            nameTextView.setText(cvar.getName());
            if (cvar.hasFlag(Variable.FLAG_NO_ARCHIVE)) {
                nameTextView.setTextColor(getResources().getColor(R.color.lunar_console_color_variable_volatile_text));
            } else {
                nameTextView.setTextColor(originalNameTextColor);
            }

            if (cvar.type == VariableType.Boolean) {
                valueEditButton.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.VISIBLE);
            } else {
                valueEditButton.setVisibility(View.VISIBLE);
                toggleSwitch.setVisibility(View.GONE);
            }

            updateUI();
        } finally {
            ignoreListenerCallbacks = false;
        }
    }

    //region OnCheckedChangeListener

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (ignoreListenerCallbacks) {
            return;
        }

        updateValue(isChecked ? "1" : "0");
    }

    //endregion

    //region View.OnClickListener

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.lunar_console_dialog);
        builder.setTitle(variable.getName());

        View contentView = LayoutInflater.from(context).inflate(R.layout.lunar_console_layout_edit_variable_dialog, null, false);
        builder.setView(contentView);

        ViewStub viewStub = contentView.findViewById(getViewStubId(variable));
        viewStub.inflate();

        final TextView defaultText = contentView.findViewById(R.id.lunar_console_edit_variable_default_value);
        defaultText.setText(String.format(getString(R.string.lunar_console_edit_variable_title_default_value), variable.defaultValue));

        if (variable.type.equals(VariableType.Enum)) {
            final String[] values = variable.values;

            Spinner spinner = contentView.findViewById(R.id.lunar_console_edit_variable_enum);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            int selectedIndex = CollectionUtils.indexOf(values, variable.value);
            spinner.setSelection(Math.max(0, selectedIndex));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateValue(values[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            final EditText valueEditText = contentView.findViewById(R.id.lunar_console_edit_variable_value);
            valueEditText.setText(variable.value);
            valueEditText.setSelectAllOnFocus(true);
            if (variable.type == VariableType.Integer) {
                valueEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }

            final SeekBar seekBar = contentView.findViewById(R.id.lunar_console_edit_variable_seek_bar);
            if (seekBar != null) {
                float floatValue = StringUtils.parseFloat(variable.value, Float.NaN);
                if (Float.isNaN(floatValue)) {
                    seekBar.setEnabled(false);
                    seekBar.setProgress(50);
                    UIUtils.showToast(context, context.getString(R.string.lunar_console_variable_value_error_message_type_float));
                } else {
                    seekBar.setProgress((int) (100 * (floatValue - variable.getMin()) / (variable.getMax() - variable.getMin())));
                }
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser || TestHelper.forceSeekBarChangeDelegate) {
                            valueEditText.setText(getValue(progress));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        String value = getValue(seekBar.getProgress());
                        valueEditText.setText(value);
                        updateValue(value);
                    }

                    private String getValue(int progress) {
                        return StringUtils.toString(variable.getMin() + 0.01f * (variable.getMax() - variable.getMin()) * progress);
                    }
                });

                valueEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == IME_ACTION_DONE || event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            float floatValue = StringUtils.parseFloat(v.getText().toString(), Float.NaN);
                            if (Float.isNaN(floatValue)) {
                                UIUtils.showToast(context, context.getString(R.string.lunar_console_variable_value_error_message_type_float));
                                return true;
                            }

                            if (floatValue < variable.getMin()) {
                                floatValue = variable.getMin();
                                v.setText(StringUtils.toString(floatValue));
                            } else if (floatValue > variable.getMax()) {
                                floatValue = variable.getMax();
                                v.setText(StringUtils.toString(floatValue));
                            }

                            seekBar.setProgress((int) (100 * (floatValue - variable.getMin()) / (variable.getMax() - variable.getMin())));
                        }

                        return false;
                    }
                });
            }
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String value = valueEditText.getText().toString();

                    switch (variable.type) {
                        case Integer: {
                            if (!StringUtils.isValidInteger(value)) {
                                UIUtils.showToast(context, context.getString(R.string.lunar_console_variable_value_error_message_type_integer));
                                return;
                            }
                            break;
                        }

                        case Float: {
                            if (!StringUtils.isValidFloat(value)) {
                                UIUtils.showToast(context,
                                        context.getString(R.string.lunar_console_variable_value_error_message_type_float));
                                return;
                            }
                            break;
                        }

                        case String: {
                            // string is always valid
                            break;
                        }

                        default: {
                            Log.e("Unexpected variable type: %s", variable.type);
                            return;
                        }
                    }

                    updateValue(value);
                }
            });
        }

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setNeutralButton(R.string.lunar_console_edit_variable_dialog_reset_to_default, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateValue(variable.defaultValue);
            }
        });
        builder.create().show();
    }

    private int getViewStubId(Variable variable) {
        if (variable.type.equals(VariableType.Enum)) {
            return R.id.lunar_console_edit_variable_enum_stub;
        }
        if (variable.hasRange()) {
            return R.id.lunar_console_edit_variable_range_stub;
        }
        return R.id.lunar_console_edit_variable_value_stub;
    }

    //endregion

    //region Helpers

    void updateValue(String value) {
        variable.value = value;
        NotificationCenter.defaultCenter().postNotification(NOTIFICATION_VARIABLE_SET, NOTIFICATION_KEY_VARIABLE, variable);
        updateUI();
    }

    void updateUI() {
        final int style = variable.isDefaultValue() ? Typeface.NORMAL : Typeface.BOLD;
        nameTextView.setTypeface(null, style);
        if (variable.type == VariableType.Boolean) {
            toggleSwitch.setTypeface(null, style);
            toggleSwitch.setChecked(variable.boolValue());
        } else {
            valueEditButton.setTypeface(null, style);
            valueEditButton.setText(variable.value);
        }
    }

    //endregion
}
