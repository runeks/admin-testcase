package no.evote.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EvoteWSException extends Exception {

	public EvoteWSException(final String msg, final Exception e) {
		super(msg, e);
	}
}
