package spacemadness.com.lunarconsole.dependency;

import spacemadness.com.lunarconsole.settings.EditorSettings;

public interface EditorSettingsProvider extends ProviderDependency {
	EditorSettings getEditorSettings();
}
