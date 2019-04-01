package spacemadness.com.lunarconsole.console;

public interface ConsolePlugin {
	void start();

	void logMessage(String message, String stackTrace, int logType);

	void showConsole();

	void hideConsole();

	void showOverlay();

	void hideOverlay();

	void clearConsole();

	void registerAction(int actionId, String actionName);

	void unregisterAction(int actionId);

	void registerVariable(int variableId, String name, String type, String value, String defaultValue, int flags, boolean hasRange, float rangeMin, float rangeMax);

	void updateVariable(int variableId, String value);

	void destroy();
}
