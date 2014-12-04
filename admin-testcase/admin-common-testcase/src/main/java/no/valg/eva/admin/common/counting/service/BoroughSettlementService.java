package no.valg.eva.admin.common.counting.service;

import java.io.Serializable;
import java.util.Map;

import no.evote.security.UserData;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.settlement.SettlementStatus;
import no.valg.eva.admin.common.counting.model.settlement.SettlementSummary;

public interface BoroughSettlementService extends Serializable {
	Map<CountCategory, SettlementStatus> buildSettlementStatus(UserData userData, ElectionPath boroughContestPath);

	SettlementSummary buildSettlementSummary(UserData userData, ElectionPath boroughContestPath);
}
