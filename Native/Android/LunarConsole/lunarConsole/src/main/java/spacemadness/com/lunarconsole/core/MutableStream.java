package spacemadness.com.lunarconsole.core;

public final class MutableStream<T> extends Stream<T> implements Disposable {
	protected MutableStream(T value) {
		super(value);
	}

	//region Factory

	public static <T> MutableStream<T> from(T value) {
		return new MutableStream<>(value);
	}

	//endregion

	//region Subscription

	@Override
	public void addValue(T value) {
		super.addValue(value);
	}

	@Override
	public void addError(Exception error) {
		super.addError(error);
	}

	//endregion

	//region Disposable

	@Override
	public void dispose() {
		close();
	}

	//endregion
}
