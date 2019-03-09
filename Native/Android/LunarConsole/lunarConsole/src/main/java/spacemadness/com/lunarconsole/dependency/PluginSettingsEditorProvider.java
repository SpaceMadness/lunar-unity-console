package spacemadness.com.lunarconsole.dependency;

import spacemadness.com.lunarconsole.settings.PluginSettingsEditor;

public interface PluginSettingsEditorProvider extends ProviderDependency {
	PluginSettingsEditor getSettingsEditor();
}
