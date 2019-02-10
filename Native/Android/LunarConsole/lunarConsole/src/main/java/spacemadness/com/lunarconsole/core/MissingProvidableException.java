package spacemadness.com.lunarconsole.core;

class MissingProvidableException extends RuntimeException {
	public MissingProvidableException(Class<? extends Providable> cls) {
		super("Providable type not registered: " + cls);
	}
}
