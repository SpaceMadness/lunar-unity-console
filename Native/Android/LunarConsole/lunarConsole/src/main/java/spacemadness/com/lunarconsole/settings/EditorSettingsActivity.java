package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import spacemadness.com.lunarconsole.R;

public class EditorSettingsActivity extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_settings);

		listView = (ListView) findViewById(R.id.lunar_console_settings_list_view);
	}
}
