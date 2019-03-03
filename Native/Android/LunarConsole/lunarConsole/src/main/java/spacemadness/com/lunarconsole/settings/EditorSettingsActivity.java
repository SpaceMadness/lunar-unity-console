package spacemadness.com.lunarconsole.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import spacemadness.com.lunarconsole.R;
import spacemadness.com.lunarconsole.core.Disposable;
import spacemadness.com.lunarconsole.dependency.PluginSettingsProvider;
import spacemadness.com.lunarconsole.dependency.Provider;
import spacemadness.com.lunarconsole.rx.AbstractObserver;
import spacemadness.com.lunarconsole.rx.Observable;
import spacemadness.com.lunarconsole.rx.Observer;

public class EditorSettingsActivity extends Activity {
	private ListView listView;
	private Disposable editorSettingsSubscription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_settings);

		final Observable<PluginSettings> settings = Provider.of(PluginSettingsProvider.class).getEditorSettings();
		editorSettingsSubscription = settings.subscribe(createObservable());

		listView = findViewById(R.id.lunar_console_settings_list_view);
	}

	private Observer<PluginSettings> createObservable() {
		return new AbstractObserver<PluginSettings>() {
			@Override
			public void onNext(PluginSettings value) {

			}
		};
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		editorSettingsSubscription.dispose();
	}
}
