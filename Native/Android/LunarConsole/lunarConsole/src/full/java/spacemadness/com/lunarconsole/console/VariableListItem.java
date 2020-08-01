package spacemadness.com.lunarconsole.console;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import spacemadness.com.lunarconsole.R;
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

    private int getBackgroundColor(Context context, int position) {
        return 0;
    }

    public static class ViewHolder extends ListViewAdapter.ViewHolder<VariableListItem> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private final View layout;
        private final TextView nameTextView;
        private final Button valueEditButton;
        private final Switch toggleSwitch;
        private final int originalNameTextColor;
        private boolean ignoreListenerCallbacks;
        private Variable variable; // TODO: weak reference?

        public ViewHolder(View convertView) {
            super(convertView);

            layout = convertView.findViewById(R.id.lunar_console_action_entry_layout);
            nameTextView = convertView.findViewById(R.id.lunar_console_variable_entry_name);
            originalNameTextColor = nameTextView.getCurrentTextColor();
            valueEditButton = convertView.findViewById(R.id.lunar_console_variable_entry_value);
            toggleSwitch = convertView.findViewById(R.id.lunar_console_variable_entry_switch);
            valueEditButton.setOnClickListener(this);
            toggleSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        protected void bindView(VariableListItem item, int position) {
            Context context = getContext();
            layout.setBackgroundColor(item.getBackgroundColor(context, position));

            Variable variable = item.variable;

            try {
                // we don't want to trigger listener callbacks while setting an initial value
                ignoreListenerCallbacks = true;

                nameTextView.setText(item.getName());

                if (variable.hasFlag(Variable.FLAG_NO_ARCHIVE)) {
                    nameTextView.setTextColor(getResources().getColor(R.color.lunar_console_color_variable_volatile_text));
                } else {
                    nameTextView.setTextColor(originalNameTextColor);
                }

                if (variable.type == VariableType.Boolean) {
                    valueEditButton.setVisibility(View.GONE);
                    toggleSwitch.setVisibility(View.VISIBLE);
                } else {
                    valueEditButton.setVisibility(View.VISIBLE);
                    toggleSwitch.setVisibility(View.GONE);
                }
            } finally {
                ignoreListenerCallbacks = false;
            }
        }

        //region View.OnClickListener

        @Override
        public void onClick(View v) {

        }

        //endregion

        //region CompoundButton.OnCheckedChangeListener

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }

        //endregion
    }
}
