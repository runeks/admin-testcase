package no.evote.service;

import java.util.Date;
import java.util.List;

import no.evote.model.views.VoterAudit;
import no.evote.security.UserData;

public interface VoterAuditService {

	List<VoterAudit> getHistoryForMunicipality(final UserData userData, final String municipalityId, final char endringstype, final Date startDate,
			final Date endDate, final Long electionEventPk, final String selectedSearchMode, final Boolean searchOnlyApproved);

	List<VoterAudit> getHistoryForVoter(final UserData userData, final long voterPk);
}
