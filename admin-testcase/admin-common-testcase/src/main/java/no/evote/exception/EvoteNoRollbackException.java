package no.evote.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = false)
public class EvoteNoRollbackException extends RuntimeException {
	public EvoteNoRollbackException() {
		super();
	}

	public EvoteNoRollbackException(final String message) {
		super(message);
	}

	public EvoteNoRollbackException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
