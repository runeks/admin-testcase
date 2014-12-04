package no.evote.exception;

public class EvoteException extends BaseException {

	protected String[] params;

	public EvoteException() {
		super();
	}

	public EvoteException(final String message, final String... params) {
		super(message);
		this.params = params;
	}

	public EvoteException(final String message, final Throwable cause, final String... params) {
		super(message, cause);
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	@Override
	public String getMessage() {
		String message = "";
		message = super.getMessage();
		return message;
	}
}
