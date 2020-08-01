package spacemadness.com.lunarconsole.console;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.debug.Log;
import spacemadness.com.lunarconsole.ui.ListViewAdapter;
import spacemadness.com.lunarconsole.ui.Switch;
import spacemadness.com.lunarconsole.utils.StringUtils;

public class VariableListItem extends EntryListItem {
    private final VariableType type;
    private final String value;
    private boolean isDefaultValue;

    public VariableListItem(int id, VariableType type, String name, String value, boolean isDefaultValue) {
        super(EntryType.Variable, id, name);
        this.type = type;
        this.value = value;
        this.isDefaultValue = isDefaultValue;
    }

    private void setChecked(boolean isChecked) {
        Log.d("Checked: %s", isChecked);
    }

    private void click() {
        Log.d("Click");
    }

    public boolean boolValue() {
        return StringUtils.parseBoolean(value);
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

            nameTextView.setText(item.name);
            nameTextView.setTextColor(item.getTextColor());

            final int style = item.isDefaultValue ? Typeface.NORMAL : Typeface.BOLD;
            nameTextView.setTypeface(null, style);
            if (item.type == VariableType.Boolean) {
                toggleSwitch.setTypeface(null, style);
                toggleSwitch.setChecked(item.boolValue());
            } else {
                valueEditButton.setTypeface(null, style);
                valueEditButton.setText(item.value);
            }

            if (item.type == VariableType.Boolean) {
                valueEditButton.setVisibility(View.GONE);
                valueEditButton.setOnClickListener(null);
                toggleSwitch.setVisibility(View.VISIBLE);
                toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.setChecked(isChecked);
                    }
                });
            } else {
                toggleSwitch.setVisibility(View.GONE);
                toggleSwitch.setOnClickListener(null);
                valueEditButton.setVisibility(View.VISIBLE);
                valueEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.click();
                    }
                });
            }
        }
    }
}
