package service.exception;
public class BadAuthenticationException extends Exception {
	public BadAuthenticationException() {
		super();
	}

	public BadAuthenticationException(String message) {
		super(message);
	}

	public BadAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadAuthenticationException(Throwable cause) {
		super(cause);
	}
}
