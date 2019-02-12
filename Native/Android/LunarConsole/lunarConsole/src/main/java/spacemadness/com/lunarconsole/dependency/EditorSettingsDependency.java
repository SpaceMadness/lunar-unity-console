package spacemadness.com.lunarconsole.dependency;

import spacemadness.com.lunarconsole.settings.EditorSettings;

public interface EditorSettingsDependency extends Dependency {
	EditorSettings getEditorSettings();
}
