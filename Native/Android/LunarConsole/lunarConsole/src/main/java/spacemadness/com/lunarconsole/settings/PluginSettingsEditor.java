package spacemadness.com.lunarconsole.settings;

import spacemadness.com.lunarconsole.dependency.ProviderDependency;

public interface PluginSettingsEditor extends ProviderDependency {
	PluginSettings getSettings();
	void setSettings(PluginSettings settings);
}
