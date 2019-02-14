package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.dependency.DependencyProvider;
import spacemadness.com.lunarconsole.dependency.EditorSettingsDependency;

public class EditorSettingsActivity extends Activity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_settings);

		final EditorSettings settings = DependencyProvider.of(EditorSettingsDependency.class).getEditorSettings();
		final EditorSettingsAdapter settingsAdapter = new EditorSettingsAdapter(settings);

		listView = findViewById(R.id.lunar_console_settings_list_view);
		listView.setAdapter(settingsAdapter);
	}
}
