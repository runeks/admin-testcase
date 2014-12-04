package no.evote.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EvoteSecurityException extends EvoteException {

	public EvoteSecurityException(final String msg) {
		super(msg);
	}

	public EvoteSecurityException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
