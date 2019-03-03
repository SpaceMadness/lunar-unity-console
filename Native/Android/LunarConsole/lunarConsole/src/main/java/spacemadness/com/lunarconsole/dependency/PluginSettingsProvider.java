package spacemadness.com.lunarconsole.dependency;

import spacemadness.com.lunarconsole.rx.Observable;
import spacemadness.com.lunarconsole.settings.PluginSettings;

public interface PluginSettingsProvider extends ProviderDependency {
	Observable<PluginSettings> getEditorSettings();
}
