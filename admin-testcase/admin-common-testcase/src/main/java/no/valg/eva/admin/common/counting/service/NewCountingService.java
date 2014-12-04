package no.valg.eva.admin.common.counting.service;

import java.io.Serializable;
import java.util.List;

import no.evote.security.UserData;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.counting.constants.ReportingUnitTypeId;
import no.valg.eva.admin.common.counting.model.CountContext;
import no.valg.eva.admin.common.counting.model.Counts;
import no.valg.eva.admin.common.counting.model.FinalCount;
import no.valg.eva.admin.common.counting.model.PreliminaryCount;
import no.valg.eva.admin.common.counting.model.ProtocolAndPreliminaryCount;
import no.valg.eva.admin.common.counting.model.ProtocolCount;
import no.valg.eva.admin.common.counting.model.modifiedballots.ModifiedBallotsStatus;

/**
 * Operations for vote counts.
 */
public interface NewCountingService extends Serializable {

	/**
	 * @return counts for given context and area
	 */
	Counts getCounts(UserData userData, CountContext context, AreaPath countingAreaPath);

	/**
	 * @return final count with status approved or to settlement for given context and area
	 */
	FinalCount findApprovedFinalCount(UserData userData, CountContext countContext, AreaPath countingAreaPath);

	/**
	 * Creates or updates a protocol count.
	 *
	 * @return protocol count with status SAVED
	 */
	ProtocolCount saveCount(UserData userData, CountContext context, ProtocolCount protocolCount);

	/**
	 * Creates or updates a protocol count and approves the count.
	 *
	 * @return protocol count with status APPROVED
	 */
	ProtocolCount approveCount(UserData userData, CountContext context, ProtocolCount protocolCount);

	/**
	 * Revokes approval on a protocol count.
	 *
	 * @return protocol count with status REVOKED
	 */
	ProtocolCount revokeCount(UserData userData, CountContext context, ProtocolCount protocolCount);

	/**
	 * Creates or updates a protocol and preliminary count.
	 *
	 * @return protocol and preliminary count with status SAVED
	 */
	ProtocolAndPreliminaryCount saveCount(UserData userData, CountContext context, ProtocolAndPreliminaryCount protocolAndPreliminaryCount);

	/**
	 * Creates or updates a protocol and preliminary count and approves the count.
	 *
	 * @return protocol and preliminary count with status APPROVED
	 */
	ProtocolAndPreliminaryCount approveCount(UserData userData, CountContext context, ProtocolAndPreliminaryCount protocolAndPreliminaryCount);

	/**
	 * Revoke approval on a protocol and preliminary count.
	 *
	 * @return protocol and preliminary count with status REVOKED
	 */
	ProtocolAndPreliminaryCount revokeCount(UserData userData, CountContext context, ProtocolAndPreliminaryCount protocolAndPreliminaryCount);

	/**
	 * Creates or updates a preliminary count.
	 *
	 * @return preliminary count with status SAVED
	 */
	PreliminaryCount saveCount(UserData userData, CountContext context, PreliminaryCount preliminaryCount);

	/**
	 * Creates or updates a preliminary count and approves the count.
	 *
	 * @return preliminary count with status APPROVED
	 */
	PreliminaryCount approveCount(UserData userData, CountContext context, PreliminaryCount preliminaryCount);

	/**
	 * Revokes approval on a preliminary count.
	 *
	 * @return preliminary count with status REVOKED
	 */
	PreliminaryCount revokeCount(UserData userData, CountContext context, PreliminaryCount preliminaryCount);

	/**
	 * Creates or updates a final count.
	 *
	 * @return final count with status SAVED
	 */
	FinalCount saveCount(UserData userData, CountContext context, FinalCount finalCount, ReportingUnitTypeId reportingUnitTypeId);

	/**
	 * Creates or updates a final count and approves the count.
	 *
	 * @return final count with status APPROVED
	 */
	FinalCount approveCount(UserData userData, CountContext context, FinalCount finalCount, ReportingUnitTypeId reportingUnitTypeId);

	/**
	 * Rejects a final count.
	 *
	 * @return final count with status REJECTED
	 */
	FinalCount rejectCount(UserData userData, CountContext context, FinalCount finalCount, ReportingUnitTypeId reportingUnitTypeId);

	/**
	 * Checks to see if an operator with specific operatorAreaPath can register Final counts
	 *
	 * @return true if Valgstyre-operator, false if not
	 */
	boolean hasFinalCountAccess(UserData userData);

	/**
	 * @return a list containing information about the number of modified ballots - in progress, remaining and completed for every ballot count belonging to
	 *         finalCount
	 */
	List<ModifiedBallotsStatus> buildModifiedBallotStatuses(UserData userData, FinalCount finalCount);

	/**
	 * Set status TO_SETTLEMENT on approved final count for a context and area.
	 * @return final count with updated status
	 */
	FinalCount updateFinalCountStatusToSettlement(UserData userData, CountContext countContext, AreaPath countingAreaPath);
}
