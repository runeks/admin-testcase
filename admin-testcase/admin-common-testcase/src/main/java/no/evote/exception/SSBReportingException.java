package no.evote.exception;

import no.evote.model.SsbReport;

//Thrown when status response from SSB web-service is unsuccessful
public class SSBReportingException extends Exception {
	private final SsbReport ssbreport;

	public SSBReportingException(final SsbReport ssbReport) {
		super(ssbReport.getStatus());
		this.ssbreport = ssbReport;
	}

	public SsbReport getFailedSSBReport() {
		return ssbreport;
	}
}
