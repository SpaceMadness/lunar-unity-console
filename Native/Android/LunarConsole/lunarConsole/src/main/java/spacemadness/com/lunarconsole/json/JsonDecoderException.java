package spacemadness.com.lunarconsole.json;

class JsonDecoderException extends RuntimeException {
	public JsonDecoderException(String message) {
		super(message);
	}

	public JsonDecoderException(Throwable cause) {
		super(cause);
	}
}
