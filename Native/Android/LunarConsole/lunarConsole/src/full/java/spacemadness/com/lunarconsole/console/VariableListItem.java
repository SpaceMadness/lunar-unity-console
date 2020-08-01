package spacemadness.com.lunarconsole.console;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.rx.Observable;
import spacemadness.com.lunarconsole.rx.Observer;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.Switch;

public class VariableListItem extends EntryListItem {
    private final Variable variable;

    public VariableListItem(Variable variable) {
        super(EntryType.Variable, variable.getActionId());
        this.variable = variable;
    }

    public String getName() {
        return variable.getName();
    }

    private void setChecked(boolean isChecked) {
        Log.d("Checked: %s", isChecked);
    }

    private void click() {
        Log.d("Click");
    }

    private int getTextColor() {
        return 0xff00ffff;
    }

    private int getBackgroundColor() {
        return 0xffffffff;
    }

    public static class ViewHolder extends ListViewAdapter.ViewHolder<VariableListItem> {
        private final View layout;
        private final TextView nameTextView;
        private final Button valueEditButton;
        private final Switch toggleSwitch;

        private Disposable subscription;

        public ViewHolder(View convertView) {
            super(convertView);

            layout = convertView.findViewById(R.id.lunar_console_action_entry_layout);
            nameTextView = convertView.findViewById(R.id.lunar_console_variable_entry_name);
            valueEditButton = convertView.findViewById(R.id.lunar_console_variable_entry_value);
            toggleSwitch = convertView.findViewById(R.id.lunar_console_variable_entry_switch);
        }

        @Override
        protected void bindView(final VariableListItem item, int position) {
            layout.setBackgroundColor(item.getBackgroundColor());

            nameTextView.setText(item.getName());
            nameTextView.setTextColor(item.getTextColor());

            subscription = item.getValueStream().subscribe(new Observer<Variable>() {
                @Override
                public void onChanged(Variable variable) {
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
            });

            if (item.variable.type == VariableType.Boolean) {
                valueEditButton.setVisibility(View.GONE);
                toggleSwitch.setVisibility(View.VISIBLE);
                toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.setChecked(isChecked);
                    }
                });
            } else {
                toggleSwitch.setVisibility(View.GONE);
                valueEditButton.setVisibility(View.VISIBLE);
                valueEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.click();
                    }
                });
            }
        }

        @Override
        protected void recycle() {
            valueEditButton.setOnClickListener(null);
            toggleSwitch.setOnCheckedChangeListener(null);
            if (subscription != null) {
                subscription.dispose();
                subscription = null;
            }
        }
    }

    private Observable<Variable> getValueStream() {
        return variable.getValueStream();
    }
}
